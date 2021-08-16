package com.wuzx.io.socket.demo1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName Server.java
 * @Description TODO
 * @createTime 2021年08月16日 09:40:00
 */
public class Server {

    public static void main(String[] args) {
        int port = 8080;

        ServerSocket serverSocket = null;
        // 绑定监听端口
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("启动服务器，监听端口：" + port);

            while (true) {

                // 服务器可以用来和客户端通信
                final Socket client = serverSocket.accept();
                System.out.println("客户端[" + client.getPort() + "] 已经连接了");

                // 创建 reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


                // 读取客户端的消息
                String msg ;

                while (null != (msg = reader.readLine())) {
                    System.out.println("客户端【" + client.getPort() + "]" + msg);

                    // 回复消息
                    writer.write("服务器：" + msg + "\n");

                    // 刷新缓冲区数据
                    writer.flush();

                    if ("quit".equals(msg)) {
                        break;
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (null != serverSocket) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
