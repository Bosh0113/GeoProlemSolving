package cn.edu.njnu.geoproblemsolving.Dao.MessageRecords;

import cn.edu.njnu.geoproblemsolving.Entity.MessageRecordsEntity;

public interface IMessageEventDao {

    String saveMessageEvent(MessageRecordsEntity messageEvent);

    Object inquiryMessageEvent(String eventType, String key, String value);
}
