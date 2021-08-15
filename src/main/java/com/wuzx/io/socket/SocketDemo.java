package com.wuzx.io.socket;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketDemo {


    /**
     * socket
     */
    @Test
    public void client() {

        int serverPort = 8080;
        Socket client = null;

       

        try {
            
            // 创建socket
            client = new Socket("localhost", serverPort);
            
            // 创建 IO流 reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            
            // 等待客户输入信息
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String input = console.readLine();
            
            // 发送消息给服务器
            writer.write(input + "\n");
            writer.flush();
            
            // 接收服务器消息
            final String msg = reader.readLine();

            System.out.println(msg);
                
        } catch (IOException e) {
            e.printStackTrace();
            
            
        }finally {
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * serverSocket
     */
    @Test
    public void Server() {
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
                final String msg = reader.readLine();

                if (null != msg) {
                    System.out.println("客户端【" + client.getPort() + "]" + msg);
                    
                    // 回复消息
                    writer.write("服务器：" + msg + "\n");
                    
                    // 刷新缓冲区数据
                    writer.flush();
                    
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
