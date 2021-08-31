package com.wuzx.io.webserver.connector;

import com.wuzx.io.webserver.processor.StaticProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName Connector.java
 * @Description socket连接
 * @createTime 2021年08月31日 09:19:00
 */
public class Connector implements Runnable{

    private ServerSocket serverSocket;
    private int port = 9999;

    public Connector() {
    }

    public Connector(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // 创建服务段
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动，监听端口：" + port);

            // 接受客户端连接
            while (true) {
                final Socket client = serverSocket.accept();

                final InputStream inputStream = client.getInputStream();
                final OutputStream outputStream = client.getOutputStream();


                // 创建request
                Request request = new Request(inputStream);
                request.parse();


                // 创建Response
                Response response = new Response(outputStream, request);

                StaticProcessor processor = new StaticProcessor();
                processor.process(request, response);
                close(client);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(serverSocket);
        }

    }

    /**
     * 启动
     */
    public void start() {
        new Thread(this).start();
    }


    public static void main(String[] args) {
        final Connector connector = new Connector();
        connector.start();
    }


    public void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
