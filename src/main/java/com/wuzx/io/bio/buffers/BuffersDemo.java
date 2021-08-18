package com.wuzx.io.bio.buffers;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName BuffersDemo.java
 * @Description 缓冲区
 * @createTime 2021年08月09日 11:08:00
 */
public class BuffersDemo {

    @Test
    public void allocate() throws FileNotFoundException {

        // 创建文件传输通道
        FileInputStream fileInputStream = new FileInputStream("E:\\Users\\admin\\Desktop\\1.jpg");
        final FileChannel inChannel = fileInputStream.getChannel();


        // 分配缓冲区
        final ByteBuffer byteBuffer = ByteBuffer.allocate(28);


        // 从缓冲区读取数据
        final byte b = byteBuffer.get();
        System.out.println(b);
    }


    /**
     * 聚集写入
     * 数据从多个缓冲区写入单个通道
     * @throws Exception
     */
    @Test
    public void gather() throws Exception {

        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        buffer1.asIntBuffer().put(1111111);


        ByteBuffer buffer2 = ByteBuffer.allocate(400);
        buffer2.asCharBuffer().put("wuzhixuan hello world");


        final GatheringByteChannel channelInstance = createChannelInstance("E:\\Users\\admin\\Desktop\\out.txt", true);

        channelInstance.write(new ByteBuffer[]{buffer1, buffer2});

    }


    @Test
    public void scatter() throws Exception {
        ByteBuffer buffer1 = ByteBuffer.allocate(8);


        ByteBuffer buffer2 = ByteBuffer.allocate(400);


        final ScatteringByteChannel scatteringByteChannel = createChannelInstance("E:\\Users\\admin\\Desktop\\out.txt", false);



        scatteringByteChannel.read(new ByteBuffer[] { buffer1, buffer2 });

        buffer1.rewind();
        buffer2.rewind();

        int bufferOne = buffer1.asIntBuffer().get();
        String bufferTwo = buffer2.asCharBuffer().toString();
        // Verification of content
        System.out.println(bufferOne);
        System.out.println(bufferTwo);
    }


    @Test
    public void transfer() throws Exception {



        FileInputStream fileInputStream = new FileInputStream("E:\\Users\\admin\\Desktop\\1.jpg");
        final FileChannel inChannel = fileInputStream.getChannel();


        FileOutputStream fileOutputStream = new FileOutputStream("E:\\Users\\admin\\Desktop\\2.jpg");
        final FileChannel outChannel = fileOutputStream.getChannel();


        inChannel.transferTo(0, inChannel.size(), outChannel);
    }


    public FileChannel createChannelInstance(String file, boolean isOut) throws FileNotFoundException {
        if (isOut) {
            return new FileOutputStream(file).getChannel();
        }

        return new FileInputStream(file).getChannel();
    }


    @Test
    public void testNew() {
        // 再jvm中开辟空间(创建一个指定capacity的ByteBuffer)
        ByteBuffer byteBuffer = ByteBuffer.allocate(24);

        // 在操作系统里面开辟空间
        final ByteBuffer direct = ByteBuffer.allocateDirect(2);

        // 容量
        final int capacity = byteBuffer.capacity();

        // 当写数据到buffer中时，limit一般和capacity相等，当读数据时，limit代表buffer中有效数据的长度。
        final int limit = byteBuffer.limit();

        // 当前读取的位置
        final int position = byteBuffer.position();

        byteBuffer.put("wuzhixuanoo".getBytes());

        System.out.println("capacity:" + byteBuffer.capacity());
        System.out.println("limit:" + byteBuffer.limit());
        System.out.println("position:" + byteBuffer.position());

        // 读数据 limit = position position = 0 ，
        // byteBuffer.flip();

        System.out.println("capacity2:" + byteBuffer.capacity());
        System.out.println("limit2:" + byteBuffer.limit());
        System.out.println("position2:" + byteBuffer.position());

        // 写数据 clear positon
        byteBuffer.clear();

        System.out.println("capacity3:" + byteBuffer.capacity());
        System.out.println("limit3:" + byteBuffer.limit());
        System.out.println("position3:" + byteBuffer.position());




    }

}
