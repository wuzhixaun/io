package com.wuzx.io.aio.demo1;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

public class AcceptHandle implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private AsynchronousServerSocketChannel serverSocketChannel;

    public AcceptHandle(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * 返回就会调用这个方法
     * @param result
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel result, Object attachment) {
        if (serverSocketChannel.isOpen()) {
            //使得服务端一直异步监听
            serverSocketChannel.accept(null, this);
        }


        // 客户端
        AsynchronousSocketChannel client = result;
        if (null == client || !client.isOpen()) {
            return;
        }

        // 读写操作
        final ClientHandler clientHandler = new ClientHandler(client);

        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Map<String, Object> map = new HashMap<>();
        map.put("type", "read");
        map.put("buffer", byteBuffer);

        client.read(byteBuffer, map, clientHandler);
    }

    /**
     * 出现异常就会调用这个方法
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, Object attachment) {
        // 处理错误情况

    }
}
