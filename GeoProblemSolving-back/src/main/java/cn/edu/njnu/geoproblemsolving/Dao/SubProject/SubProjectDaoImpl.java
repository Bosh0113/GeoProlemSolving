package cn.edu.njnu.geoproblemsolving.Dao.SubProject;

import cn.edu.njnu.geoproblemsolving.Commen.FileStructConst;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.utils.EditJsonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class SubProjectDaoImpl implements ISubProjectDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubProjectDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object createSubProject(SubProjectEntity subProject) {
        try {
            Date data = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String subProjectId = UUID.randomUUID().toString();
            subProject.setSubProjectId(subProjectId);
            subProject.setMembers(new JSONArray());
            subProject.setCreateTime(dateFormat.format(data));
            JSONObject fileStructJson = new JSONObject();
            fileStructJson.put("folders",new JSONArray());
            fileStructJson.put("files",new JSONArray());
            fileStructJson.put("uid",subProjectId);
            String fileStruct = fileStructJson.toJSONString();
            subProject.setFileStruct(fileStruct);

            // decode projectId
            String pid = subProject.getProjectId();
            if (pid.length() > 36) {
                String projectId = new String(EncodeUtil.decode(pid));
                subProject.setProjectId(projectId.substring(0, projectId.length() - 2));
            }
            Query queryUser = Query.query(Criteria.where("userId").is(subProject.getManagerId()));
            UserEntity managerInfo = mongoTemplate.findOne(queryUser, UserEntity.class);
            subProject.setManagerName(managerInfo.getUserName());
            mongoTemplate.save(subProject);
            subProject.setProjectId(pid);
            return subProject;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readSubProject(String key, String value) {
        try {
            // decode
            String projectId = value;
            if (projectId.length() > 36) {
                String pid = new String(EncodeUtil.decode(projectId));
                value = pid.substring(0, pid.length() - 2);
            }

            Query query = new Query(Criteria.where(key).is(value));
            if (mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                return "None";
            } else {
                List<SubProjectEntity> subProjectEntities = mongoTemplate.find(query, SubProjectEntity.class);

                for (int i = 0; i < subProjectEntities.size(); i++) {
                    // get
                    SubProjectEntity subProjectEntity = subProjectEntities.get(i);
                    projectId = subProjectEntity.getProjectId();
                    // encode
                    if (projectId.length() == 36) {
                        String randomID = UUID.randomUUID().toString().substring(0, 2);
                        projectId = EncodeUtil.encode((projectId + randomID).getBytes());
                    }
                    // set
                    subProjectEntity.setProjectId(projectId);
                }

                return subProjectEntities;
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String deleteSubProject(String key, String value) {
        try {
            Query query = new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query, SubProjectEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updateSubProject(HttpServletRequest request) {
        try {
            String subProjectId = request.getParameter("subProjectId");
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
            return mongoTemplate.findOne(query,SubProjectEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object joinSubProject(String subProjectId, String userId) {
        try {
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
                String managerId = subProject.getManagerId();
                JSONArray members = subProject.getMembers();
                CommonMethod method = new CommonMethod();
                Query queryUser=Query.query(Criteria.where("userId").is(userId));
                UserEntity userEntity=mongoTemplate.findOne(queryUser,UserEntity.class);
                Object result = method.joinGroup(members, managerId, userId,userEntity.getUserName());
                if (result.equals("Exist")) {
                    return "Exist";
                } else {
                    Update update = new Update();
                    update.set("members", result);
                    mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                    return mongoTemplate.findOne(query, SubProjectEntity.class);
                }
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String quitSubProject(String subProjectId, String userId) {
        try {

            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            if (!mongoTemplate.find(query, SubProjectEntity.class).isEmpty()) {
                SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
                JSONArray members = subProject.getMembers();
                CommonMethod method = new CommonMethod();
                JSONArray newMembers = method.quitGroup(members, userId, "userId");
                Update update = new Update();
                update.set("members", newMembers);
                mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
                return "Success";
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object changeManager(String subProjectId, String userId) {
        try {
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            SubProjectEntity subProject = mongoTemplate.findOne(query, SubProjectEntity.class);
            String foreManagerId = subProject.getManagerId();
            String foreManagerName=subProject.getManagerName();
            JSONArray members = subProject.getMembers();
            CommonMethod method = new CommonMethod();
            JSONArray newMembers = method.quitGroup(members, userId, "userId");
            JSONArray newMembers1 = (JSONArray) method.joinGroup(newMembers, userId, foreManagerId,foreManagerName);
            Query queryUser =Query.query(Criteria.where("userId").is(userId));
            UserEntity newManager=mongoTemplate.findOne(queryUser,UserEntity.class);
            Update update = new Update();
            update.set("members", newMembers1);
            update.set("managerId", newManager.getUserId());
            update.set("managerName",newManager.getUserName());
            mongoTemplate.updateFirst(query, update, SubProjectEntity.class);
            return mongoTemplate.findOne(query, SubProjectEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }


    @Override
    public String updateFileStruct(String subProjectId, String fileStruct) {
        try {
            Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
            Update update = Update.update("fileStruct", fileStruct);
            mongoTemplate.updateFirst(query,update,SubProjectEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String getFileStruct(String subProjectId) {
        String fileStruct="";
        Query query = new Query(Criteria.where("subProjectId").is(subProjectId));
        SubProjectEntity entity = mongoTemplate.findOne(query, SubProjectEntity.class);
        if(entity!=null && entity.getFileStruct()!=null){
            fileStruct = entity.getFileStruct();
        }
        if(fileStruct.isEmpty()){
            return "Fail";
        }
        return fileStruct;
    }

    @Override
    public String createFolder(String subProjectId, String parentId, String name) {
        try {
            // decode
            if (subProjectId.length() > 36) {
                String pId = new String(EncodeUtil.decode(subProjectId));
                subProjectId = pId.substring(0, pId.length() - 2);
            }
            String id = UUID.randomUUID().toString();
            String fileStruct = getFileStruct(subProjectId);
            String newFileStruct = EditJsonUtil.updateFileStruct(fileStruct, subProjectId, parentId, id, name, FileStructConst.CREATE_FOLDER);
            if("Fail".equals(newFileStruct)){
                return "Fail";
            }
            return newFileStruct;
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteFolder(String subProjectId, String parentId, String id) {
        // decode
        if (subProjectId.length() > 36) {
            String pId = new String(EncodeUtil.decode(subProjectId));
            subProjectId = pId.substring(0, pId.length() - 2);
        }
        String fileStruct = getFileStruct(subProjectId);
        String newFileStruct = EditJsonUtil.updateFileStruct(fileStruct, subProjectId, parentId, id, "", FileStructConst.DELETE_FOLDER);
        if("Fail".equals(newFileStruct)){
            return "Fail";
        }
        return newFileStruct;
    }

    @Override
    public String renameFolder(String subProjectId, String parentId, String id, String name) {
        // decode
        if (subProjectId.length() > 36) {
            String pId = new String(EncodeUtil.decode(subProjectId));
            subProjectId = pId.substring(0, pId.length() - 2);
        }
        String fileStruct = getFileStruct(subProjectId);
        String newFileStruct = EditJsonUtil.updateFileStruct(fileStruct, subProjectId, parentId, id, name, FileStructConst.RENAME_FOLDER);
        if("Fail".equals(newFileStruct)){
            return "Fail";
        }
        return newFileStruct;
    }

    @Override
    public String deleteFile(String subProjectId, String parentId, String id) {
        // decode
        if (subProjectId.length() > 36) {
            String pId = new String(EncodeUtil.decode(subProjectId));
            subProjectId = pId.substring(0, pId.length() - 2);
        }
        String fileStruct = getFileStruct(subProjectId);
        String newFileStruct = EditJsonUtil.updateFileStruct(fileStruct, subProjectId, parentId, id, "", FileStructConst.DELETE_FILE);
        if("Fail".equals(newFileStruct)){
            return "Fail";
        }
        return newFileStruct;
    }

    public String createFolderTree(){
        try {
            List<SubProjectEntity> subProjectEntities = mongoTemplate.findAll(SubProjectEntity.class);
            for (SubProjectEntity subProjectEntity:subProjectEntities){
                Query query =Query.query(Criteria.where("scope.subProjectId").is(subProjectEntity.getSubProjectId()));
                List<ResourceEntity> resourceEntities = mongoTemplate.find(query,ResourceEntity.class);
                JSONObject fileStruct = new JSONObject();
                JSONArray fileList = new JSONArray();
                for (ResourceEntity resourceEntity:resourceEntities){
                    JSONObject fileInfo = new JSONObject();
                    fileInfo.put("name",resourceEntity.getName());
                    fileInfo.put("uid",resourceEntity.getResourceId());
                    fileList.add(fileInfo);
                }
                JSONArray folderList = new JSONArray();
                fileStruct.put("name",subProjectEntity.getTitle());
                fileStruct.put("uid",subProjectEntity.getSubProjectId());
                fileStruct.put("files",fileList);
                fileStruct.put("folders",folderList);
                Update update = new Update();
                update.set("fileStruct",fileStruct.toJSONString());
                Query query1= Query.query(Criteria.where("subProjectId").is(subProjectEntity.getSubProjectId()));
                mongoTemplate.updateFirst(query1,update,SubProjectEntity.class);
            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
