package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public interface IResourceDao {

    Object saveResource(HttpServletRequest request);

    Object readResource(String key,String value);

    String deleteResource(String key,String value);

    void getZipResource(HttpServletRequest request, HttpServletResponse response, String key, String value);
}
