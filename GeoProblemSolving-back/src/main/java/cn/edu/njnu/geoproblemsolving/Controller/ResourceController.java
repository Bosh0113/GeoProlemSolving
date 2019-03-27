package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Object uploadResource(HttpServletRequest request){
        ResourceDaoImpl resourceDao=new ResourceDaoImpl(mongoTemplate);
        return resourceDao.saveResource(request);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readResource(@RequestParam("key") String key,@RequestParam("value") String value){
        ResourceDaoImpl resourceDao=new ResourceDaoImpl(mongoTemplate);
        return resourceDao.readResource(key,value);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteResource(@RequestParam("resourceId") String resourceId){
        ResourceDaoImpl resourceDao=new ResourceDaoImpl(mongoTemplate);
        return resourceDao.deleteResource("resourceId",resourceId);
    }
}
