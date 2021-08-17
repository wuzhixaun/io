package com.wuzx.io.socket.demo2.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器
 */
public class ChatServer {

    private int DEFAULT_PORT = 8080;
    private final String QUIT = "exit";

    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;


    public ChatServer() {
        connectedClients = new HashMap<>();
    }


    /**
     * 添加客户端
     * @param socket
     * @throws IOException
     */
    public void addClient(Socket socket) throws IOException {
        if (null == socket) {
            return;
        }

        final int port = socket.getPort();

        // 创建write对象
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        connectedClients.put(port, writer);

        System.out.println(String.format("客户端%s加入服务器", port));
    }

    /**
     * 删除客户端
     * @param socket
     */
    public void removeClient(Socket socket) throws IOException {
        if (null == socket) {
            return;
        }

        final int port = socket.getPort();

        if (connectedClients.containsKey(port)) {
            connectedClients.get(port).close();
        }

        connectedClients.remove(port);
    }

    /**
     * 转发消息
     * @param socket
     * @param fwdMsg
     * @throws IOException
     */
    public void forwardMessage(Socket socket, String fwdMsg) throws IOException {
        for (Integer curPort : connectedClients.keySet()) {
            if (curPort.equals(socket.getPort())) {
                continue;
            }
            final Writer writer = connectedClients.get(curPort);
            writer.write(fwdMsg + "\n");
            writer.flush();
        }
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
        if (null == serverSocket) {
            return;
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            // 绑定默认端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println(String.format("启动服务器，监听端口%s", DEFAULT_PORT));

            while (true) {

                // 等待客户端连接
                final Socket client = serverSocket.accept();

                // 创建chathandle线程
                new Thread(new ChatHandler(this, client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }


}
