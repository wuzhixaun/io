package com.wuzx.io.nio.chatroom;


import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatClient {
    private static int DEFAULT_PORT = 8080;
    private static String DEFAULT_HOST = "127.0.0.1";
    private static String EXIT = "exit";
    private static final int BUFFER_SIZE = 1024;

    // 客户端channel
    private SocketChannel client;
    // 多路复用器
    private Selector selector;
    // 缓冲区 读/写
    private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    Charset charset = Charset.forName("UTF-8");

    public ChatClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public ChatClient(String host,int port) {
        DEFAULT_HOST = host;
        DEFAULT_PORT = port;
    }


    /**
     * 启动
     */
    public void start() {
        try {
            // 创建客户端client
            client = SocketChannel.open();

            // 设置非阻塞
            client.configureBlocking(false);

            // 创建多路复用器
            selector = Selector.open();

            // 注册-(只监听连接请求)
            client.register(selector, SelectionKey.OP_CONNECT);

            // 连接
            client.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));

            while (true) {
                selector.select();   // 这一步会阻塞，直到监听时间被触发
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(this::handleSelectionKey);
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }

    }



    /**
     * 处理channel对应的key
     * @param selectionKey 相当于channel的id
     */
    private void handleSelectionKey(SelectionKey selectionKey){

        try {
            if (selectionKey.isConnectable()) { // 链接就绪

                final SocketChannel client = (SocketChannel) selectionKey.channel();
                if (client.isConnectionPending()) {
                    client.finishConnect();

                    new Thread(new UserInputHandler(this)).start();
                }
                // 注册到多路复用器
                client.register(selector , SelectionKey.OP_READ);
            } else if (selectionKey.isReadable()) { // 服务器转发的消息
                final SocketChannel client = (SocketChannel) selectionKey.channel();
                String msg = receive(client);
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


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
     * @param
     */
    public void close() {
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (msg.isEmpty()) {
            return;
        }

        try {
            writeBuffer.clear();
            writeBuffer.put(charset.encode(msg));
            writeBuffer.flip();
            while (writeBuffer.hasRemaining()) {
                client.write(writeBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
