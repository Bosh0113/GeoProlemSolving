package cn.edu.njnu.geoproblemsolving.Socket;

import cn.edu.njnu.geoproblemsolving.Config.GetHttpSessionConfigurator;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/Module/{moduleId}", configurator = GetHttpSessionConfigurator.class)
public class ModuleSocket {
    private Session session = null;
    private static final Map<String, Map<String, ModuleSocket>> modules = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("moduleId") String moduleId, Session session, EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (!modules.containsKey(moduleId)) {
            Map<String, ModuleSocket> module = new ConcurrentHashMap<>();
            module.put(httpSession.getAttribute("userId").toString(), this);
            modules.put(moduleId, module);
        } else {
            modules.get(moduleId).put(httpSession.getAttribute("userId").toString(), this);
        }
        broadcastOnlineToModule(moduleId,httpSession.getAttribute("userId").toString());
        broadcastMembersToModule(moduleId);
    }

    @OnMessage
    public void onMessage(@PathParam("moduleId") String moduleId, String message) {
        JSONObject messageObject = JSONObject.parseObject(message);
        String messageType = messageObject.getString("type");
        if (messageType.equals("message")) {
            broadcastMessageToModule(moduleId, message);
        }
    }

    @OnClose
    public void onClose(@PathParam("moduleId") String moduleId) {
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            if (server.getValue().equals(this)) {
                broadcastOfflineToModule(moduleId,server.getKey());
                modules.get(moduleId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(moduleId);
    }

    @OnError
    public void onError(@PathParam("moduleId") String moduleId, Throwable error) {
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            if (server.getValue().equals(this)) {
                broadcastOfflineToModule(moduleId,server.getKey());
                modules.get(moduleId).remove(server.getKey());
                break;
            }
        }
        broadcastMembersToModule(moduleId);
    }

    private void broadcastMembersToModule(String moduleId) {
        ArrayList<String> members = new ArrayList<>();
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            members.add(server.getKey());
        }
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "members");
        messageObject.put("message", members.toString());
        String message = messageObject.toString();
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessageToModule(String moduleId, String message) {
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastOnlineToModule(String moduleId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "online");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            try {
                server.getValue().session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcastOfflineToModule(String moduleId, String userId){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", "offline");
        messageObject.put("userId",userId);
        messageObject.put("createTime",dateFormat.format(date));
        String message = messageObject.toString();
        for (Map.Entry<String, ModuleSocket> server : modules.get(moduleId).entrySet()) {
            try {
                if(!server.getKey().equals(userId)){
                    server.getValue().session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
