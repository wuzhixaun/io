package com.wuzx.io.bio.chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 聊天客户端
 */
public class ChatHandler implements Runnable{

    private ChatServer chatServer;
    private Socket client;

    public ChatHandler(ChatServer chatServer, Socket client) {
        this.chatServer = chatServer;
        this.client = client;
    }

    @Override
    public void run() {

        try {
            // 添加到在线列表
            chatServer.addClient(client);

            // 读取服务器消息（读取群聊消息）
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String msg = null;

            while (null != (msg = reader.readLine())) {
                String fwdMsg = String.format("客户端[%s]-消息【%s】", client.getPort(), msg);
                System.out.println(msg);

                // 消息转发到群聊
                chatServer.forwardMessage(client, fwdMsg);

                // 检查用户是否退出
                if (chatServer.readyExit(msg)) {
                    chatServer.removeClient(client);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
