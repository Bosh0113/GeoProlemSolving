package cn.edu.njnu.geoproblemsolving.Entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Resource")
public class ResourceEntity {

    private String resourceId;
    private JSONObject scope;         //projectId/subProjectId
    private String name;
    private String description;
    private String belong;
    private String type;
    private String fileSize;
    private String pathURL;
    private String uploaderId;
    private String uploaderName;
    private String uploadTime;
    private String organization;
    private String privacy;     //public/private

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

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

    public void setScope(JSONObject scope) {
        this.scope = scope;
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

    public JSONObject getScope() {
        return scope;
    }

    public String getBelong() {
        return belong;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public String getOrganization() {
        return organization;
    }

    public String getPrivacy() {
        return privacy;
    }
}
