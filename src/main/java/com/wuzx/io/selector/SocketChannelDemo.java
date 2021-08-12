package com.wuzx.io.selector;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName SocketChannelDemo.java
 * @Description TODO
 * @createTime 2021年08月09日 14:51:00
 */
public class SocketChannelDemo {
    private InetSocketAddress sd = new InetSocketAddress("localhost", 8888);

    @Test
    public void server() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();

        server.bind(sd);


        while (true) {
            // 接受客户端链接
            final SocketChannel client = server.accept();

            System.out.println("链接成功" + client);
        }
    }

    @Test
    public void client() throws IOException {
        SocketChannel client = SocketChannel.open();
        // 链接服务端
        client.connect(sd);
    }
}
