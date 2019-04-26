package cn.edu.njnu.geoproblemsolving.Entity;

import java.util.ArrayList;

/**
 * Created by SongJie on 2019/4/26 0:27
 */
public class ResourceUploadInfo {
    private String FileName;
    private ArrayList<String> ZipFiles;
    private String resourceId;

    public void setFileName(String uploaderId) {
        this.FileName = uploaderId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setZipFiles(ArrayList<String> ZipFiles) {
        this.ZipFiles = ZipFiles;
    }

    public ArrayList<String> getZipFiles() {
        return ZipFiles;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
