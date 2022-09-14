package com.sz.blockchain.net;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class P2pServer {

    private int port = 7000;

    private List<WebSocket> socketList = new ArrayList<>();

    public void initServer(){
        WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(port)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                sendMessage(webSocket, "服务器创建了连接");
                socketList.add(webSocket);
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                socketList.remove(webSocket);
            }

            //收到客户端发送过来的消息
            @Override
            public void onMessage(WebSocket webSocket, String s) {
                sendMessage(webSocket, "收到消息");
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                socketList.remove(webSocket);
            }

            @Override
            public void onStart() {
                System.out.println("服务器websocket server启动了");
            }
        };

        webSocketServer.start();
    }

    public void sendMessage(WebSocket webSocket, String message){
        System.out.println("发送给" + webSocket.getRemoteSocketAddress().getHostName() + webSocket.getRemoteSocketAddress().getPort() + "端口的p2p消息是" + message);
        webSocket.send(message);
    }

    public List<WebSocket> getSocketList() {
        return socketList;
    }

    public void setSocketList(List<WebSocket> socketList) {
        this.socketList = socketList;
    }

    public void broadcast(String message){
        if(socketList.size() == 0){
            return;
        }
        for (WebSocket webSocket : socketList) {
            this.sendMessage(webSocket, message);
        }
    }
}
