package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import javax.servlet.http.HttpServletRequest;

public interface IResourceDao {

    String saveResource(HttpServletRequest request);

    Object readResource(String key,String value);

    String deleteResource(String key,String value);

}
