package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Commen.FileStructConst;
import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.Dao.SubProject.SubProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceUploadInfo;
import cn.edu.njnu.geoproblemsolving.utils.EditJsonUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadResource(HttpServletRequest request) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.saveResource(request);
    }

    @RequestMapping(value = "/subProjectUpload", method = RequestMethod.POST)
    public Object uploadSubProjResource(HttpServletRequest request) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        ResourceUploadInfo uploadInfo;
        try {
            ArrayList<ResourceUploadInfo> uploadInfos= (ArrayList<ResourceUploadInfo>) resourceDao.saveResource(request);
            if(uploadInfos==null || uploadInfos.size()==0){
                return "Fail";
            }
            uploadInfo = uploadInfos.get(0);
        }catch (Exception e){
            return "Fail";
        }

        String subProjectId = request.getParameter("subProjectId");
        String parentId = request.getParameter("parentId");
        //更新subProject fileStruct
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        //1 获取subProject fileStruct
        String fileStruct = subProjectDao.getFileStruct(subProjectId);
        String newFileStruct = EditJsonUtil.updateFileStruct(fileStruct, subProjectId,parentId, uploadInfo.getResourceId(), uploadInfo.getFileName(),FileStructConst.UPLOAD_FILE);
        String result = subProjectDao.updateFileStruct(subProjectId,newFileStruct);
        if(!"Fail".equals(result)){
            return newFileStruct;
        }else{
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readResource(@RequestParam("key") String key, @RequestParam("value") String value) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.readResource(key, value);
    }

    @RequestMapping(value = "/allPublic", method = RequestMethod.GET)
    public Object readAllPublic() {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.readPublicResource();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteResource(@RequestParam("resourceId") String resourceId) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.deleteResource("resourceId", resourceId);
    }

    @RequestMapping(value = "/operateZip", method = RequestMethod.GET)
    public void getZipResource(HttpServletRequest request, HttpServletResponse response, @RequestParam("key") String key, @RequestParam("value") String value) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        resourceDao.getZipResource(request, response, key, value);
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public Object shareResource(HttpServletRequest request) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.shareResource(request);
    }
}
