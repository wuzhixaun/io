package com.wuzx.io.aio.demo1;



import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {


    private final static String DEFAULT_HOST = "localhost";
    private final static int DEFAULT_PORT = 8080;
    private AsynchronousSocketChannel socketChannel;

    public void start() {
        try {
            // 创建
            socketChannel = AsynchronousSocketChannel.open();
            // 连接
            final Future<Void> future = socketChannel.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));

            // 这是一个阻塞的调用
            future.get();

            // 发送给服务器的消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                final String input = reader.readLine();
                final byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

                final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                final Future<Integer> result = socketChannel.write(byteBuffer);

                // 阻塞调用
                result.get();


                byteBuffer.flip();
                final Future<Integer> readResult = socketChannel.read(byteBuffer);
                readResult.get();

                System.out.println(new String(byteBuffer.array()));
                byteBuffer.clear();
            }

        } catch (IOException e) {


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close(socketChannel);

        }
    }

    private void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("关闭" + closeable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
