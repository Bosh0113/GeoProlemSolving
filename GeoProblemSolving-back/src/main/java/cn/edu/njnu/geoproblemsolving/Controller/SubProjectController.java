package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.SubProject.SubProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.SubProjectEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/subProject")
public class SubProjectController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public Object createSubProject(@RequestBody SubProjectEntity subProject){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.createSubProject(subProject);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readSubProject(@RequestParam("key") String key,@RequestParam("value") String value){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.readSubProject(key,value);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteSubProject(@RequestParam("subProjectId") String subProjectId){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.deleteSubProject("subProjectId",subProjectId);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateSubProject(HttpServletRequest request){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.updateSubProject(request);
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public Object joinSubProject(@RequestParam("subProjectId") String subProjectId,@RequestParam("userId") String userId){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.joinSubProject(subProjectId,userId);
    }

    @RequestMapping(value = "/quit", method = RequestMethod.GET)
    public String quitSubProject(@RequestParam("subProjectId") String subProjectId,@RequestParam("userId") String userId){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.quitSubProject(subProjectId,userId);
    }

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public Object changeManager(@RequestParam("subProjectId") String subProjectId,@RequestParam("userId") String userId){
        SubProjectDaoImpl subProjectDao=new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.changeManager(subProjectId,userId);
    }

    @RequestMapping(value = "/updateFileStrcut", method = RequestMethod.POST)
    public String updateFileStruct(@RequestParam("subProjectId") String subProjectId,@RequestParam("fileStruct") String fileStruct){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.updateFileStruct(subProjectId,fileStruct);
    }

    @RequestMapping(value = "/getFileStrcut", method = RequestMethod.GET)
    public String getFileStruct(@RequestParam("subProjectId") String subProjectId){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        return subProjectDao.getFileStruct(subProjectId);
    }



    @RequestMapping(value = "/createFolder", method = RequestMethod.POST)
    public String createFolder(@RequestParam("subProjectId") String subProjectId,@RequestParam("parentId") String parentId,@RequestParam("folderName") String folderName){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        String struct = subProjectDao.createFolder(subProjectId, parentId, folderName);
        if("Fail".equals(struct)){
            return "Fail";
        }
        return subProjectDao.updateFileStruct(subProjectId,struct);
    }

    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST)
    public String deleteFolder(@RequestParam("subProjectId") String subProjectId,@RequestParam("parentId") String parentId,@RequestParam("folderUid") String folderUid){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        String struct = subProjectDao.deleteFolder(subProjectId, parentId, folderUid);
        if("Fail".equals(struct)){
            return "Fail";
        }
        return subProjectDao.updateFileStruct(subProjectId,struct);
    }
    @RequestMapping(value = "/renameFolder", method = RequestMethod.POST)
    public String renameFolder(@RequestParam("subProjectId") String subProjectId,@RequestParam("parentId") String parentId,@RequestParam("folderName") String folderName,@RequestParam("folderUid") String folderUid){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        String struct = subProjectDao.renameFolder(subProjectId, parentId,folderUid, folderName);
        if("Fail".equals(struct)){
            return "Fail";
        }
        return subProjectDao.updateFileStruct(subProjectId,struct);
    }
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public String deleteFile(@RequestParam("subProjectId") String subProjectId,@RequestParam("parentId") String parentId,@RequestParam("fileUid") String fileUid){
        SubProjectDaoImpl subProjectDao = new SubProjectDaoImpl(mongoTemplate);
        String struct = subProjectDao.deleteFile(subProjectId, parentId, fileUid);
        if("Fail".equals(struct)){
            return "Fail";
        }
        return subProjectDao.updateFileStruct(subProjectId,struct);
    }
}
