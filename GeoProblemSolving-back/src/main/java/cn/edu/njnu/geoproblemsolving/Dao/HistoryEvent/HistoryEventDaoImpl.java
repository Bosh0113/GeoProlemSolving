package cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent;

import cn.edu.njnu.geoproblemsolving.Dao.Method.EncodeUtil;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class HistoryEventDaoImpl implements IHistoryEventDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public HistoryEventDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveHistoryEvent(HistoryEventEntity historyEvent){
        try {
            // decode subprojectId
            String sid = historyEvent.getScopeId();
            String scopeId = new String(EncodeUtil.decode(sid));
            historyEvent.setScopeId(scopeId.substring(0,scopeId.length()-2));

            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            historyEvent.setCreateTime(dateFormat.format(date));
            historyEvent.setHistoryId(UUID.randomUUID().toString());
            mongoTemplate.save(historyEvent);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryHistoryEvent(String scopeId){
        try {
            // decode scopeId
            String sId = scopeId;
            scopeId = new String(EncodeUtil.decode(sId));
            scopeId = scopeId.substring(0,scopeId.length()-2);

            Query query=new Query(Criteria.where("scopeId").is(scopeId));

            // encode scopeId
            List<HistoryEventEntity> historyEventEntities = mongoTemplate.find(query,HistoryEventEntity.class);
            for(int i = 0;i < historyEventEntities.size();i++){
                // get
                HistoryEventEntity historyEventEntity = historyEventEntities.get(i);
                scopeId = historyEventEntity.getScopeId();

                // encode
                String randomID = UUID.randomUUID().toString().substring(0,2);
                scopeId = EncodeUtil.encode((scopeId + randomID).getBytes());

                // set
                historyEventEntity.setScopeId(scopeId);
            }
            return historyEventEntities;

        }catch (Exception e){
            return "Fail";
        }
    }
}
