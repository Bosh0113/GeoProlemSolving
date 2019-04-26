package cn.edu.njnu.geoproblemsolving.Dao.SubProject;

import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;

import javax.servlet.http.HttpServletRequest;

public interface ISubProjectDao {
    Object createSubProject(SubProjectEntity subProject);
    Object readSubProject(String key,String value);
    String deleteSubProject(String key,String value);
    Object updateSubProject(HttpServletRequest request);
    Object joinSubProject(String subProjectId,String userId);
    String quitSubProject(String subProjectId,String userId);
    Object changeManager(String subProjectId,String userId);
    String updateFileStruct(String subProjectId,String fileStruct);
    String getFileStruct(String subProjectId);

    String createFolder(String projectId, String parentId, String name);

    String deleteFolder(String projectId, String parentId, String id);

    String renameFolder(String projectId,String parentId,String id,String name);

    String deleteFile(String projectId,String parentId,String id);
}
