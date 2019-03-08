package cn.edu.njnu.geoproblemsolving.Dao.Project;

import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class ProjectDaoImpl implements IProjectDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProjectDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createProject(ProjectEntity project){
        mongoTemplate.save(project);
        return project.getProjectId();
    }

    @Override
    public Object readProject(String key,String value){
        if(key.equals("projectId")){
            // decode
            String pid = value;
            if(pid.length() > 36) {
                String projectId = new String(EncodeUtil.decode(pid));
                value = projectId.substring(0, projectId.length() - 2);
            }
        }
        Query query=Query.query(Criteria.where(key).is(value));
        if(mongoTemplate.find(query,ProjectEntity.class).isEmpty()){
            return "None";
        }
        else {
            List<ProjectEntity> projectEntities = mongoTemplate.find(query,ProjectEntity.class);

            for(int i = 0;i < projectEntities.size();i++){
                // get
                ProjectEntity projectEntity = projectEntities.get(i);
                String projectId = projectEntity.getProjectId();

                // encode
                if(projectId.length() == 36) {
                    String randomID = UUID.randomUUID().toString().substring(0, 2);
                    projectId = EncodeUtil.encode((projectId + randomID).getBytes());
                }

                // set
                projectEntity.setProjectId(projectId);
            }

            return projectEntities;
        }
    }

    @Override
    public void deleteProject(String key,String value){

        // decode
        String pid = value;
        if(pid.length() > 36) {
            String projectId = new String(EncodeUtil.decode(pid));
            value = projectId.substring(0, projectId.length() - 2);
        }

        Query query=Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query,"Project");
    }

    @Override
    public String updateProject(HttpServletRequest request){
        try{
            // decode
            String projectId = request.getParameter("projectId");
            if(projectId.length() > 36) {
                String pid = new String(EncodeUtil.decode(projectId));
                projectId = pid.substring(0, pid.length() - 2);
            }

            Query query=new Query(Criteria.where("projectId").is(projectId));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            update.set("projectId",projectId);
            mongoTemplate.updateFirst(query,update,ProjectEntity.class);
            return "Success";
        }
        catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object joinProject(String projectId,String userId){
        try{
            // decode
            if(projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query queryProject=new Query(Criteria.where("projectId").is(projectId));
            if (!mongoTemplate.find(queryProject,ProjectEntity.class).isEmpty()){
                ProjectEntity project=mongoTemplate.findOne(queryProject,ProjectEntity.class);
                String managerId=project.getManagerId();
                JSONArray members=project.getMembers();
                CommonMethod method=new CommonMethod();
                Object result=method.joinGroup(members,managerId,userId,mongoTemplate);
                if (result.equals("Exist")){
                    return "Exist";
                }
                else {
                    Update update=new Update();
                    update.set("members",result);
                    mongoTemplate.updateFirst(queryProject,update,ProjectEntity.class);

                    Query queryUser=new Query(Criteria.where("userId").is(userId));
                    UserEntity user=mongoTemplate.findOne(queryUser,UserEntity.class);
                    JSONArray joinedProjects=user.getJoinedProjects();
                    JSONObject newProjectInfo=new JSONObject();
                    newProjectInfo.put("projectId",project.getProjectId());
                    newProjectInfo.put("title",project.getTitle());
                    joinedProjects.add(newProjectInfo);
                    Update updateUser=new Update();
                    updateUser.set("joinedProjects",joinedProjects);
                    mongoTemplate.updateFirst(queryUser,updateUser,UserEntity.class);
                    return "Success";
                }
            }
            else {
                return "None";
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String quitProject(String projectId,String userId){
        try {
            // decode
            if(projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query queryProject=new Query(Criteria.where("projectId").is(projectId));
            if (!mongoTemplate.find(queryProject,ProjectEntity.class).isEmpty()){
                ProjectEntity project=mongoTemplate.findOne(queryProject,ProjectEntity.class);
                JSONArray members=project.getMembers();
                CommonMethod method=new CommonMethod();
                JSONArray newMembers=method.quitGroup(members,userId,"userId");
                Update updateProject=new Update();
                updateProject.set("members",newMembers);
                mongoTemplate.updateFirst(queryProject,updateProject,ProjectEntity.class);

                Query queryUser =new Query(Criteria.where("userId").is(userId));
                UserEntity user= mongoTemplate.findOne(queryUser,UserEntity.class);
                JSONArray joinedProjects=user.getJoinedProjects();
                JSONArray newJoinedProjects=method.quitGroup(joinedProjects,projectId,"projectId");
                Update updateUser=new Update();
                updateUser.set("joinedProjects",newJoinedProjects);
                mongoTemplate.updateFirst(queryUser,updateUser,UserEntity.class);

                return "Success";
            }
            else {
                return "None";
            }

        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object changeManager(String projectId,String userId){
        try {
            // decode
            if(projectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(projectId));
                projectId = pId.substring(0, pId.length() - 2);
            }

            Query query =new Query(Criteria.where("projectId").is(projectId));
            ProjectEntity project=mongoTemplate.findOne(query,ProjectEntity.class);
            String foreManagerId= project.getManagerId();
            JSONArray members=project.getMembers();
            CommonMethod method=new CommonMethod();
            JSONArray newMembers=method.quitGroup(members,userId,"userId");
            Query queryUser=new Query(Criteria.where("userId").is(foreManagerId));
            UserEntity user=mongoTemplate.findOne(queryUser,UserEntity.class);
            JSONObject foreManager=new JSONObject();
            foreManager.put("userId",user.getUserId());
            foreManager.put("userName",user.getUserName());
            newMembers.add(foreManager);
            Update update=new Update();
            update.set("managerId",userId);
            update.set("members",newMembers);
            mongoTemplate.updateFirst(query,update,ProjectEntity.class);
            return mongoTemplate.findOne(query,ProjectEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }
}
