package com.wuzx.io.nio.chatroom;

import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatServer {

    private static int DEFAULT_PORT = 8080;
    private static String EXIT = "exit";
    private static final int BUFFER_SIZE = 1024;


    // 服务端channel
    private ServerSocketChannel server;
    // 多路复用器
    private Selector selector;
    // 缓冲区 读/写
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    Charset charset = Charset.forName("UTF-8");



    public ChatServer() {
        this(DEFAULT_PORT);
    }

    public ChatServer(int port) {
        DEFAULT_PORT = port;
    }

    /**
     * 启动服务端
     */
    public void start() {

        try {
            // 创建Sever
            server = ServerSocketChannel.open();
            // 设置非阻塞
            server.configureBlocking(false);
            // 绑定端口
            server.socket().bind(new InetSocketAddress(DEFAULT_PORT));

            // 创建多路选择器
            selector = Selector.open();

            // 设置监听事件(接收客户端连接事件)
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println(String.format("聊天服务器启动，监听端口【%s】", DEFAULT_PORT));


            while (true) {
                selector.select(); // 阻塞-等待发生监听事件
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(this::handleSelectionKey);
                selectionKeys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 虽然只关闭了Selector，但是可以把相关通道都关闭
            close(selector);
        }

    }

    /**
     * 处理channel对应的key
     * @param selectionKey 相当于channel的id
     */
    private void handleSelectionKey(SelectionKey selectionKey) {

        try {
            if (selectionKey.isAcceptable()) {// 与客户端建立连接
                final SocketChannel clientChannel = server.accept();
                clientChannel.configureBlocking(false);
                // 将客户端channel注册到多路复用器-只关注可读时间
                clientChannel.register(selector, SelectionKey.OP_READ);
                System.out.println(getClientName(clientChannel.socket())+"已连接");
            } else if (selectionKey.isReadable()) {// channel可读
                final SocketChannel clientChannel = (SocketChannel) selectionKey.channel();

                // 解析发送的消息
                String msg = receive(clientChannel);

                System.out.println(getClientName(clientChannel.socket()) + ":" + msg);
                if (StringUtils.isEmpty(msg)) {
                    selectionKey.cancel();
                    selector.wakeup();
                } else {
                    // 转发消息
                    forwardMsg(clientChannel, msg);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发消息
     *
     * @param currentClient
     * @param msg
     */
    private void forwardMsg(SocketChannel currentClient, String msg) {

        // 获取多路复用器上面所有注册的key
        final Set<SelectionKey> keys = selector.keys();

        keys.forEach(x -> {
            try {
                if (!x.isValid() || x.channel().equals(currentClient)) {
                    return;
                }

                // 客户端的才需要转发
                if (x.channel() instanceof ServerSocketChannel) {
                    return;
                }

                final SocketChannel client = (SocketChannel) x.channel();

                writeBuffer.clear();
                writeBuffer.put(charset.encode(getClientName(client.socket()) + ":" + msg));


                // 开始读数据
                writeBuffer.flip();

                while (writeBuffer.hasRemaining()) {
                    client.write(writeBuffer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * 从通道中读取数据
     * @param clientChannel
     * @return
     */
    private String receive(SocketChannel clientChannel) throws IOException {
        readBuffer.clear();
        while (clientChannel.read(readBuffer) > 0) ;
        readBuffer.flip();
        return String.valueOf(charset.decode(readBuffer));
    }


    /**
     * 得到客户端名称
     * @param client
     * @return
     */
    private String getClientName(Socket client) {
        return String.format("客户端【%s】", client.getPort());
    }

    /**
     * 准备退出
     * @param msg
     * @return
     */
    public boolean readyExit(String msg) {
        return EXIT.equals(msg);
    }

    /**
     * 关闭资源
     * @param closeable
     */
    private void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
