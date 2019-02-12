package cn.edu.njnu.geoproblemsolving.Dao.Method;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class CommonMethod {

    public Update setUpdate(HttpServletRequest request) {
        Update update = new Update();
        Enumeration parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String key = (String) parameters.nextElement();
            String value = request.getParameter(key);
            update.set(key, value);
        }
        return update;
    }

    public Object joinGroup(JSONArray members, String managerId, String userId, MongoTemplate mongoTemplate){
        Boolean exist=false;
        if(members.size()>0){
            if (userId.equals(managerId)){
                exist=true;
            }
            else {
                for (int i=0;i<members.size();i++){
                    JSONObject member=members.getJSONObject(i);
                    if (userId.equals(member.getString("userId"))){
                        exist=true;
                        break;
                    }
                }
            }
        }
        if (exist){
            return "Exist";
        }
        else {
            Query query=new Query(Criteria.where("userId").is(userId));
            UserEntity user=mongoTemplate.findOne(query,UserEntity.class);
            JSONObject newMember=new JSONObject();
            newMember.put("userId",user.getUserId());
            newMember.put("userName",user.getUserName());
            members.add(newMember);

            return members;
        }
    }

    public JSONArray quitGroup(JSONArray group,String memberId,String idType){
        JSONArray newGroup=new JSONArray();
        for (int i=0;i<group.size();i++){
            JSONObject member=group.getJSONObject(i);
            if (memberId.equals(member.getString(idType))){
                continue;
            }
            newGroup.add(member);
        }
        return newGroup;
    }
}
