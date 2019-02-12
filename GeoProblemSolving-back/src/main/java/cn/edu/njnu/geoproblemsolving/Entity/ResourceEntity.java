package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Resource")
public class ResourceEntity {

    private String resourceId;
    private String scopeId;         //public:projectId/private:subProjectId
    private String name;
    private String description;
    private String type;
    private String fileSize;
    private String pathURL;
    private String uploaderId;
    private String uploadTime;

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPathURL(String pathURL) {
        this.pathURL = pathURL;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPathURL() {
        return pathURL;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public String getFileSize() {
        return fileSize;
    }
}
