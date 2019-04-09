package cn.edu.njnu.geoproblemsolving.Dao.Task;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class TaskDaoImpl implements ITaskDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveTask(TaskEntity task){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            task.setTaskId(UUID.randomUUID().toString());
            task.setCreateTime(dateFormat.format(date));
            mongoTemplate.save(task);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryTask(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            if (!mongoTemplate.find(query,TaskEntity.class).isEmpty()){
                return mongoTemplate.find(query,TaskEntity.class);
            }
            else {
                return "None";
            }
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryTodo(String subprojectId){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("subprojectId").is(subprojectId),Criteria.where("state").is("todo"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryDoing(String subprojectId){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("subprojectId").is(subprojectId),Criteria.where("state").is("doing"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryDone(String subprojectId){
        try {
            Query query=new Query();
            Criteria criteria=new Criteria();
            criteria.andOperator(Criteria.where("subprojectId").is(subprojectId),Criteria.where("state").is("done"));
            query.addCriteria(criteria);
            query.with(new Sort(Sort.Direction.ASC,"order"));
            return mongoTemplate.find(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteTask(String taskId){
        try {
            Query query=new Query(Criteria.where("taskId").is(taskId));
            mongoTemplate.remove(query,TaskEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object updateTask(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("taskId").is(request.getParameter("taskId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,TaskEntity.class);
            return mongoTemplate.findOne(query,TaskEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }
}
