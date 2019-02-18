package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Task")
public class TaskEntity {
    private String moduleId;
    private String taskId;
    private String taskName;
    private String description;
    private Date startTime;
    private Date endTime;
    private String state;   //_todo; doing; done.
    private String creatorId;
    private String createTime;
    private String order;

    public String getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getState() {
        return state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
