package cn.edu.njnu.geoproblemsolving.Dao.User;


import cn.edu.njnu.geoproblemsolving.Dao.Method.AESUtils;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


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
        Query queryPhone = Query.query(Criteria.where("mobilePhone").is(user.getMobilePhone()));
        if (!mongoTemplate.find(queryEmail, UserEntity.class).isEmpty()) {
            return "Email";
        } else if (!mongoTemplate.find(queryPhone, UserEntity.class).isEmpty()) {
            return "MobilePhone";
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
    public Object login(String email, String password) {
        AESUtils aesUtils=new AESUtils();
        password=aesUtils.decrypt(password);
        Query query = new Query(Criteria.where("email").is(email).and("password").is(password));
        if (!mongoTemplate.find(query, UserEntity.class).isEmpty()) {
            UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
            user.setPassword("");
            return user;
        } else {
            return "Fail";
        }
    }
}
