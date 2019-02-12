package cn.edu.njnu.geoproblemsolving.Dao.Task;

import cn.edu.njnu.geoproblemsolving.Entity.TaskEntity;

import javax.servlet.http.HttpServletRequest;

public interface ITaskDao {

    String saveTask(TaskEntity task);

    Object inquiryTask(String key,String value);

    Object inquiryTodo(String moduleId);

    Object inquiryDoing(String moduleId);

    Object inquiryDone(String moduleId);

    String deleteTask(String taskId);

    Object updateTask(HttpServletRequest request);
}
