package com.wuzx.io.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName ConnectTest.java
 * @Description TODO
 * @createTime 2021年08月31日 10:06:00
 */
public class ConnectTest {

    private static final String VALID_REQUEST_URI = "GET /index1.html HTTP/1.1";

    public static void main(String[] args) {
        // 创建client
        try {
            Socket client = new Socket("localhost", 9999);

            // 发送请求
            final OutputStream outputStream = client.getOutputStream();
            outputStream.write(VALID_REQUEST_URI.getBytes());
            client.shutdownOutput();

            // 接受响应
            final InputStream inputStream = client.getInputStream();
            byte[] buffer = new byte[2048];
            int length = inputStream.read(buffer);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append((char) buffer[i]);
            }

            client.shutdownInput();
            client.close();

            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
