package cn.edu.njnu.geoproblemsolving.Dao.SubProject;

import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SubProjectDaoImpl implements ISubProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubProjectDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createSubProject(SubProjectEntity subProject){
        try {
            mongoTemplate.save(subProject);
            return subProject.getSubProjectId();
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object readSubProject(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            if (mongoTemplate.find(query,SubProjectEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,SubProjectEntity.class);
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteSubProject(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query,SubProjectEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateSubProject(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("subProjectId").is(request.getParameter("subProjectId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object joinSubProject(String subProjectId,String userId){
        try {
            Query query=new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query,SubProjectEntity.class).isEmpty()){
                SubProjectEntity subProject=mongoTemplate.findOne(query,SubProjectEntity.class);
                String managerId=subProject.getManagerId();
                JSONArray members=subProject.getMembers();
                CommonMethod method=new CommonMethod();
                Object result=method.joinGroup(members,managerId,userId,mongoTemplate);
                if (result.equals("Exist")){
                    return "Exist";
                }
                else {
                    Update update=new Update();
                    update.set("members",result);
                    mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
                    return mongoTemplate.findOne(query,SubProjectEntity.class);
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
    public String quitSubProject(String subProjectId,String userId){
        try {
            Query query=new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query,SubProjectEntity.class).isEmpty()){
                SubProjectEntity subProject=mongoTemplate.findOne(query,SubProjectEntity.class);
                JSONArray members=subProject.getMembers();
                CommonMethod method=new CommonMethod();
                JSONArray newMembers=method.quitGroup(members,userId,"userId");
                Update update= new Update();
                update.set("members",newMembers);
                mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
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
    public Object changeManager(String subProjectId,String userId){
        try {
            Query query=new Query(Criteria.where("subProjectId").is(subProjectId));
            SubProjectEntity subProject=mongoTemplate.findOne(query,SubProjectEntity.class);
            String foreManagerId=subProject.getManagerId();
            JSONArray members=subProject.getMembers();
            CommonMethod method=new CommonMethod();
            JSONArray newMembers=method.quitGroup(members,userId,"userId");
            JSONArray newMembers1=(JSONArray)method.joinGroup(newMembers,userId,foreManagerId,mongoTemplate);
            Update update=new Update();
            update.set("members",newMembers1);
            update.set("managerId",userId);
            mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
            return mongoTemplate.findOne(query,SubProjectEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }
}
