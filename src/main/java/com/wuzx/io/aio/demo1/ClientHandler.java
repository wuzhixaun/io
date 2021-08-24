package com.wuzx.io.aio.demo1;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;

public class ClientHandler implements CompletionHandler<Integer, Object> {

    private AsynchronousSocketChannel client;

    public ClientHandler(AsynchronousSocketChannel client) {
        this.client = client;
    }

    @Override
    public void completed(Integer result, Object attachment) {
        Map<String, Object> map = (Map<String, Object>) attachment;
        final String type = (String) map.get("type");

        ByteBuffer buffer = (ByteBuffer) map.get("buffer");
        if ("read".equals(type)) {
            buffer.flip();
            map.put("type", "write");
            client.write(buffer, map, this);
            buffer.clear();
        } else if ("write".equals(type)) {
            // 继续监听
            final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            map.put("type", "read");
            map.put("buffer", byteBuffer);
            client.read(byteBuffer, map, this);
        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
