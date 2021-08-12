package com.wuzx.io.channel;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName ChannelDemo.java
 * @Description 通道
 * @createTime 2021年08月09日 10:48:00
 */
public class ChannelDemo {


    /**
     * 文件通道用于从文件读取数据。它只能通过调用getChannel()方法来创建对象。不能直接创建FileChannel对象
     */
    @Test
    public void FileChannel() throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream("E:\\Users\\admin\\Desktop\\1.jpg");
            final FileChannel inChannel = fileInputStream.getChannel();


            FileOutputStream fileOutputStream = new FileOutputStream("E:\\Users\\admin\\Desktop\\2.jpg");
            final FileChannel outChannel = fileOutputStream.getChannel();


            copyData(inChannel, outChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void copyData(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(20 * 1024);
        while (src.read(buffer) != -1) {
            // The buffer is used to drained
            buffer.flip();
            // keep sure that buffer was fully drained
            while (buffer.hasRemaining()) {
                dest.write(buffer);
            }
            buffer.clear(); // Now the buffer is empty, ready for the filling
        }
    }



    /**
     * 数据报通道可以通过UDP(用户数据报协议)通过网络读取和写入数据。它使用工厂方法来创建新对象
     */
    @Test
    public void datagramChannel() {
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();


            datagramChannel.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 数据报通道可以通过TCP(传输控制协议)通过网络读取和写入数据。 它还使用工厂方法来创建新对象。
     */
    @Test
    public void socketChannel() {

        try {
            final SocketChannel socketChannel = SocketChannel.open();
            socketChannel.bind(new InetSocketAddress("localhost", 8888));

            socketChannel.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void serverSocketChannel() {
        try {
            final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();


            serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
