package cn.edu.njnu.geoproblemsolving.Dao.User;


import cn.edu.njnu.geoproblemsolving.Dao.Method.AESUtils;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
public class UserDaoImpl implements IUserDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String saveUser(UserEntity user) {
        Query queryEmail = Query.query(Criteria.where("email").is(user.getEmail()));
//        Query queryPhone = Query.query(Criteria.where("mobilePhone").is(user.getMobilePhone()));
        if (!mongoTemplate.find(queryEmail, UserEntity.class).isEmpty()) {
            return "Email";
//        } else if (!mongoTemplate.find(queryPhone, UserEntity.class).isEmpty()) {
//            return "MobilePhone";
        } else {
            mongoTemplate.save(user);
            return user.getUserId();
        }
    }

    @Override
    public Object readUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, UserEntity.class).isEmpty()) {
            return "None";
        } else {
            UserEntity user = mongoTemplate.findOne(query, UserEntity.class);

            user.setPassword("");
            return user;
        }
    }

    @Override

    public void removeUser(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, "User");
    }

    @Override
    public Object updateUser(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("userId").is(request.getParameter("userId")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, UserEntity.class);
            return mongoTemplate.findOne(query, UserEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updatePassword(String email,String password){
        try {
            if (!isRegistered(email)){
                return "None";
            }else {
                Query query = new Query(Criteria.where("email").is(email));
                Update updatePassword=new Update();
                updatePassword.set("password",password);
                mongoTemplate.updateFirst(query, updatePassword, UserEntity.class);
                return mongoTemplate.findOne(query, UserEntity.class);
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object login(String email, String password) {
        if(isRegistered(email)){
            AESUtils aesUtils=new AESUtils();
            password=aesUtils.decrypt(password);
            if (verifyPassword(email,password)){
                Query query = new Query(Criteria.where("email").is(email));
                UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
                user.setPassword("");
                return user;
            }
            else {
                return "Password";
            }
        }
        else {
            return "Email";
        }
    }

    @Override
    public Boolean isRegistered(String email){
        Query query = new Query(Criteria.where("email").is(email));
        return !mongoTemplate.find(query, UserEntity.class).isEmpty();
    }

    @Override
    public Boolean verifyPassword(String email,String password){
        Query query = new Query(Criteria.where("email").is(email).and("password").is(password));
        return !mongoTemplate.find(query, UserEntity.class).isEmpty();
    }

    public String updateKey(){
        try {
            List<UserEntity> userList = mongoTemplate.findAll(UserEntity.class);
            for (UserEntity user:userList){
                String userId = user.getUserId();
                Query queryUser = Query.query(Criteria.where("userId").is(userId));
                JSONArray manageProjects = new JSONArray();
                Query queryProject = Query.query(Criteria.where("managerId").is(userId));
                List<ProjectEntity> projectEntityList=mongoTemplate.find(queryProject,ProjectEntity.class);
                for (ProjectEntity projectEntity:projectEntityList){
                    JSONObject projectInfo =new JSONObject();
                    projectInfo.put("title",projectEntity.getTitle());
                    projectInfo.put("projectId",projectEntity.getProjectId());
                    manageProjects.add(projectInfo);
                }
                Update updateUser = new Update();
                updateUser.set("manageProjects",manageProjects);
                mongoTemplate.updateFirst(queryUser,updateUser,UserEntity.class);
            }
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
