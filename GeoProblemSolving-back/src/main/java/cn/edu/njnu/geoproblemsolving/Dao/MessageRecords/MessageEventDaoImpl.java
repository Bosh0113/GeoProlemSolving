package cn.edu.njnu.geoproblemsolving.Dao.MessageRecords;

import cn.edu.njnu.geoproblemsolving.Dao.MessageRecords.IMessageEventDao;
import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class MessageEventDaoImpl implements IMessageEventDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageEventDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveMessageEvent(MessageRecordsEntity messageEvent){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            messageEvent.setCreateTime(dateFormat.format(date));
            messageEvent.setMessageId(UUID.randomUUID().toString());
            mongoTemplate.save(messageEvent);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryMessageEvent(String eventType, String key, String value){
        try {

            Query query=new Query(Criteria.where("eventType").is(eventType).and(key).is(value));
            if (mongoTemplate.find(query,MessageRecordsEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,MessageRecordsEntity.class);
            }

        }catch (Exception e){
            return "Fail";
        }
    }
}
