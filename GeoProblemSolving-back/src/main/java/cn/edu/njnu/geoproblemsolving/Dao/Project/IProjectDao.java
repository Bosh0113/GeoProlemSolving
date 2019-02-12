package cn.edu.njnu.geoproblemsolving.Dao.Project;

import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;

import javax.servlet.http.HttpServletRequest;

public interface IProjectDao {
    String createProject(ProjectEntity project);

    Object readProject(String key,String value);

    void deleteProject(String key,String value);

    String updateProject(HttpServletRequest request);

    Object joinProject(String projectId,String userId);

    String quitProject(String projectId,String userId);

    Object changeManager(String projectId,String userId);
}
