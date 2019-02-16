package cn.edu.njnu.geoproblemsolving.Socket;

import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/DrawServer/{roomId}")
public class DrawServerSocket {

    private Session session=null;

    //初始化集合，用来存放每个客户端对应的服务器端的WebSocket对象
    private static CopyOnWriteArraySet<DrawServerSocket> servers=new CopyOnWriteArraySet<>();

    //定义了当一个新用户连接成功后所调用的方法
    @OnOpen
    public void onOpen(Session session)
    {
        this.session=session;
        servers.add(this);
        System.out.println("连接已经建立,sessionID:"+session.getId());

    }
    //接收消息后所调用的方法
    @OnMessage
    public void onMessage(String message)
    {
        try
        {
            //向客户端发送消息
            for (DrawServerSocket server:servers)
            {
                server.session.getBasicRemote().sendText(message);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @OnClose
    public void onClose()
    {
        servers.remove(this);
        System.out.println("连接已经关闭");
    }
    @OnError
    public void onError(Session session,Throwable error)
    {
        System.out.println("发生错误");
        error.printStackTrace();
    }
}

