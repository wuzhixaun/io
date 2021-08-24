package com.wuzx.io.aio.demo1;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Server {


    private final static String DEFAULT_HOST = "localhost";
    private final static int DEFAULT_PORT = 8080;
    private AsynchronousServerSocketChannel serverSocketChannel;


    private void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("关闭" + closeable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            // 创建
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            // 绑定监听端口
            serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));
            System.out.println("服务器启动。。。端口：" + DEFAULT_PORT);


            while (true) {
                // 等待客户端连接
                serverSocketChannel.accept(null,new AcceptHandle(serverSocketChannel));
                System.in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(serverSocketChannel);
        }


    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }


}
