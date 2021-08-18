package com.wuzx.io.bio.selector;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName SelectorDemo.java
 * @Description TODO
 * @createTime 2021年08月09日 14:18:00
 */
public class SelectorDemo {
    private InetSocketAddress sd = new InetSocketAddress("localhost", 8888);

    @Test
    public void server() throws IOException {


        // 创建选择器
        final Selector selector = Selector.open();
        System.out.println("Selector is open for making connection: " + selector.isOpen());


        // ServerSocketChannel
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.bind(sd);
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 向选择其注册channel
        serverSocketChannel.register(selector, serverSocketChannel.validOps());

        while (true) {
            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            final Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {

                final SelectionKey next = iterator.next();
                System.out.println("SelectionKey:---" + next);
                if (next.isAcceptable()) {
                    final SocketChannel client = serverSocketChannel.accept();
                    client.configureBlocking(false);
                    // 将客户端注册到selector
                    client.register(selector, SelectionKey.OP_ACCEPT);
                    System.out.println("The new connection is accepted from the client: " + client);
                } else if (next.isReadable()) {
                    SocketChannel client = (SocketChannel) next.channel();
                    ;
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String output = new String(buffer.array()).trim();
                    System.out.println("Message read from client: " + output);
                } else if (next.isConnectable()) {
                    System.out.println("The connection was established with a remote server");

                } else if (next.isWritable()) {
                    System.out.println("准备好写入了  ");
                }

                iterator.remove();
            }
        }
    }

    @Test
    public void client() throws Exception {
        SocketChannel client = SocketChannel.open();
        // 链接服务端
        final boolean connect = client.connect(sd);


        System.out.println("connet" + connect);


        ByteBuffer bb= ByteBuffer.allocate(48);
        String newData = "data:  " + System.currentTimeMillis();
        bb.clear();
        bb.put(newData.getBytes());
        bb.flip();
        while(bb.hasRemaining()) {
            System.out.println(bb);
            client.write(bb);
        }

    }
}
