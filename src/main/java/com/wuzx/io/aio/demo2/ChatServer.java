package com.wuzx.io.aio.demo2;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {


    private static final String HOST = "localhost";
    private int DEFAULT_PORT = 8888;
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;
    private static final int THREAD_POOL_SIZE = 8;

    private AsynchronousServerSocketChannel socketChannel;
    private AsynchronousChannelGroup channelGroup;
    private Charset charset = Charset.forName("UTF-8");

    public ChatServer(int port) {
        this.DEFAULT_PORT = port;
    }

    public ChatServer() {

    }

    private boolean readyQuit(String msg) {
        return QUIT.equals(msg);
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start() {

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
            socketChannel = AsynchronousServerSocketChannel.open(channelGroup);

            // 绑定端口
            socketChannel.bind(new InetSocketAddress(HOST, DEFAULT_PORT));
            System.out.println("服务器启动，监听端口：" + DEFAULT_PORT);


            while (true) {
                socketChannel.accept(null, new AcceptHandle());
                System.in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }


    /**
     * 客户端接收
     */
    private class AcceptHandle implements CompletionHandler<AsynchronousSocketChannel, Object> {
        @Override
        public void completed(AsynchronousSocketChannel client, Object attachment) {

            // 如果服务端不关闭，则一直监听
            if (socketChannel.isOpen()) {
                socketChannel.accept(null, this);
            }

            if (client == null || !client.isOpen()) {
                return;
            }

            ClientHandler clientHandler = new ClientHandler();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER);

            client.write(buffer, buffer, clientHandler);
        }

        @Override
        public void failed(Throwable exc, Object attachment) {

        }
    }

    private class ClientHandler implements CompletionHandler<>{


    }
}
