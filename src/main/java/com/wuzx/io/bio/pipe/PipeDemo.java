package com.wuzx.io.bio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName PipeDemo.java
 * @Description 管道： 用于按顺序读取和写入数据。管道用于确保数据必须以写入管道的相同顺序读取。
 * @createTime 2021年08月09日 17:16:00
 */
public class PipeDemo {

    public static void main(String[] args) throws IOException {

        // 创建管道
        Pipe pipe = Pipe.open();


        // 获取接收器管道
        final Pipe.SinkChannel sink = pipe.sink();
        String newData = "The new String is writing in a Pipe..." + System.currentTimeMillis();
        ByteBuffer bb= ByteBuffer.allocate(512);
        bb.clear();
        bb.put(newData.getBytes());
        bb.flip();
        while(bb.hasRemaining()) {
            sink.write(bb);
        }

        // 从管道读取数据: 要从管道读取数据，需要先访问源通道。
        // 获取源通道
        final Pipe.SourceChannel source = pipe.source();
        bb= ByteBuffer.allocate(512);
        while (source.read(bb) > 0) {
            bb.flip();

            while (bb.hasRemaining()) {
                char TestData = (char) bb.get();
                System.out.print(TestData);
            }

            bb.clear();
        }


    }

}
