package com.wuzx.io.socket.demo2.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 聊天客户端
 */
public class ChatClient {


    private String host = "127.0.0.1";
    private int DEFAULT_PORT = 8080;
    private final String QUIT = "exit";
    private Socket socket;
    BufferedWriter writer;
    BufferedReader reader;

    public Socket getSocket() {
        return socket;
    }

    /**
     * 发送消息到聊天室
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            if (socket.isOutputShutdown()) {
                return;
            }
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 接收消息
     * @return
     */
    public String receiveMsg() {

        try {
            if (socket.isInputShutdown()) {
                return null;
            }
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 准备退出
     * @param msg
     * @return
     */
    public boolean readyExit(String msg) {
        return QUIT.equals(msg);
    }


    /**
     * 关闭资源
     */
    public void close() {
        if (null == socket) {
            return;
        }

        try {
            socket.close();
            System.out.println(String.format("关闭client[%s]", socket.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动
     */
    public void start() {

        try {
            socket = new Socket(host, DEFAULT_PORT);
            System.out.println(String.format("客户端连接[%s]", socket.getPort()));
            // 获取IO流
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // 监听用户输入
            new Thread(new UserInputHandler(this)).start();

            // 读取聊天室信息
            String msg =null ;
            while (null != (msg = receiveMsg())) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }
}
