package cn.edu.njnu.geoproblemsolving.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MessageRecords")
public class MessageRecordsEntity {

    private String messageId;
    private String roomId; //查询条件Id
    private String content;
    private String userId;
    private String createTime;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setMessageId(String historyId) {
        this.messageId = historyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getContent() {
        return content;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }

}
