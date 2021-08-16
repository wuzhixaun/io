package com.wuzx.io.socket.demo1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName Client.java
 * @Description TODO
 * @createTime 2021年08月16日 09:40:00
 */
public class Client {

    public static void main(String[] args) {
        int serverPort = 8080;
        Socket client = null;



        try {

            // 创建socket
            client = new Socket("localhost", serverPort);

            // 创建 IO流 reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            // 等待客户输入信息
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String input = console.readLine();

                // 发送消息给服务器
                writer.write(input + "\n");
                writer.flush();

                // 接收服务器消息
                final String msg = reader.readLine();

                System.out.println(msg);
                if ("quit".equals(msg)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();


        }finally {
            if (null != client) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
