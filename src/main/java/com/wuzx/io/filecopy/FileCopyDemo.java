package com.wuzx.io.filecopy;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName FileCopyDemo.java
 * @Description 文件拷贝示例
 * @createTime 2021年08月18日 10:07:00
 */
public class FileCopyDemo {


    /**
     * 一个字节一个字节拷贝
     */
    @Test
    public void noBufferFileCopy() {

        final FileCopyRunner fileCopyRunner = (Source, target) -> {

            try {
                // 创建输入流
                InputStream fin = new FileInputStream(Source);

                // 创建输出流
                OutputStream fou = new FileOutputStream(target);

                int read;
                while ((read = fin.read()) != -1) {
                    fou.write(read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        fileCopyRunner.copyFile(new File("E:\\Users\\admin\\Desktop\\1.jpg"), new File("E:\\Users\\admin\\Desktop\\3.jpg"));
    }


    /**
     * 缓冲字节拷贝
     */

    @Test
    public void bufferFileCopy() {

        FileCopyRunner fileCopyRunner = (Source, target) -> {

            try {
                // 创建输入流
                InputStream fin = new FileInputStream(Source);

                // 创建输出流
                OutputStream fou = new FileOutputStream(target);

                byte[] bytes = new byte[2048];

                // 每次读取的字节个数
                int read;
                while ((read = fin.read(bytes)) != -1) {
                    System.out.println(read);
                    fou.write(bytes, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        };

        fileCopyRunner.copyFile(new File("E:\\Users\\admin\\Desktop\\1.jpg"), new File("E:\\Users\\admin\\Desktop\\4.jpg"));
    }
}