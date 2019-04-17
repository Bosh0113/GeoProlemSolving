package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Bulletin")
public class BulletinEntity {
    private String bulletinId;
    private String moduleId;
    private String title;
    private String description;
    private String creatorId;
    private String creatorName;
    private Date time;

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Date getTime() {
        return time;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public String getModuleId() {
        return moduleId;
    }
}
