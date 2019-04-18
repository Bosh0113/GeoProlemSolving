package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Task.TaskDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.TaskEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public Object saveTask(@RequestBody TaskEntity task){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.saveTask(task);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryTask(@RequestParam("key") String key,@RequestParam("value") String value){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.inquiryTask(key,value);
    }

    @RequestMapping(value = "/inquiryTodo", method = RequestMethod.GET)
    public Object inquiryTodo(@RequestParam("subprojectId") String subprojectId){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.inquiryTodo(subprojectId);
    }

    @RequestMapping(value = "/inquiryDoing", method = RequestMethod.GET)
    public Object inquiryDoing(@RequestParam("subprojectId") String subprojectId){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.inquiryDoing(subprojectId);
    }

    @RequestMapping(value = "/inquiryDone", method = RequestMethod.GET)
    public Object inquiryDone(@RequestParam("subprojectId") String subprojectId){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.inquiryDone(subprojectId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteTask(@RequestParam("taskId") String taskId){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.deleteTask(taskId);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateTask(HttpServletRequest request){
        TaskDaoImpl taskDao=new TaskDaoImpl(mongoTemplate);
        return taskDao.updateTask(request);
    }
}
