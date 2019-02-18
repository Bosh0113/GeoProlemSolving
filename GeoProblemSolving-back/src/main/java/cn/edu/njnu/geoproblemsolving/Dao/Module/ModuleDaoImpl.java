package cn.edu.njnu.geoproblemsolving.Dao.Module;

import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.ModuleEntity;
import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@Component
public class ModuleDaoImpl implements IModuleDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ModuleDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createModule(ModuleEntity module){

        // decode subprojectId
        String spid = module.getSubProjectId();
        String subProjectId = new String(EncodeUtil.decode(spid));
        module.setSubProjectId(subProjectId.substring(0,subProjectId.length()-2));

        mongoTemplate.save(module);
        return module.getModuleId();
    }

    @Override
    public Object readModule(String key,String value){

        // decode subprojectId
        String spid = value;
        String subProjectId = new String(EncodeUtil.decode(spid));
        value = subProjectId.substring(0,subProjectId.length()-2);

        Query query=Query.query(Criteria.where(key).is(value));
        if(mongoTemplate.find(query,ModuleEntity.class).isEmpty()){
            return "None";
        }else {

            List<ModuleEntity> ModuleEntities = mongoTemplate.find(query,ModuleEntity.class);

            for(int i = 0;i < ModuleEntities.size();i++){
                // get
                ModuleEntity moduleEntity = ModuleEntities.get(i);
                subProjectId = moduleEntity.getSubProjectId();

                // encode
                String randomID = UUID.randomUUID().toString().substring(0,2);
                subProjectId = EncodeUtil.encode((subProjectId + randomID).getBytes());

                // set
                moduleEntity.setSubProjectId(subProjectId);
            }
            return ModuleEntities;
        }
    }

    @Override
    public void deleteModule(String key,String value){
        Query query=Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query,ModuleEntity.class);
    }

    @Override
    public String updateModule(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("moduleId").is(request.getParameter("moduleId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,ModuleEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
