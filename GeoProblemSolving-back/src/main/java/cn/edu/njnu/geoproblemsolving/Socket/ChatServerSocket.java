package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import cn.edu.njnu.geoproblemsolving.Config.MyEndPointConfigure;
import cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent.HistoryEventDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ChatServer/{roomId}", configurator = MyEndPointConfigure.class)
public class ChatServerSocket {

    private static final Map<String,Map<String,Session>> rooms=new ConcurrentHashMap<>();

    @Autowired
    MongoTemplate mongoTemplate;

    @OnOpen
    public void onOpen(@PathParam("roomId") String roomId, Session session,EndpointConfig config)
    {
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!rooms.containsKey(roomId)){
            Map<String,Session> room=new ConcurrentHashMap<>();
            room.put(httpSession.getAttribute("userId").toString(), session);
            rooms.put(roomId,room);
        }
        else {
            rooms.get(roomId).put(httpSession.getAttribute("userId").toString(), session);
        }
        broadcastMembersToRoom(roomId);
    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, String message, Session session)
    {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        if (messageType.equals("message")) {
            broadcastMessageToRoom(roomId, message, session);

            // 将历史消息进行存储
            try {
                HistoryEventDaoImpl historyEventDao = new HistoryEventDaoImpl(mongoTemplate);
                HistoryEventEntity historyEventEntity = new HistoryEventEntity();
                historyEventEntity.setScopeId(roomId);
                historyEventEntity.setEventType("message");
                historyEventEntity.setDescription(message);
                historyEventDao.saveHistoryEvent(historyEventEntity);
            }
            catch (Exception ex) {
                throw ex;
            }
        }
    }
    @OnClose
    public void onClose(@PathParam("roomId") String roomId, Session session)
    {
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }
    @OnError
    public void onError(@PathParam("roomId") String roomId, Session session,Throwable error)
    {
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (server.getValue().equals(session)) {
                rooms.get(roomId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToRoom(roomId);
    }

    private void broadcastMembersToRoom(String roomId){
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            try {
                server.getValue().getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void broadcastMessageToRoom(String roomId,String message, Session session){
        for (Map.Entry<String, Session> server : rooms.get(roomId).entrySet()) {
            if (!session.equals(server.getValue())) {
                try {
                    server.getValue().getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
