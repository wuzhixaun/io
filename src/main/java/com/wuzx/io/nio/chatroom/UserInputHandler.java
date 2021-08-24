package com.wuzx.io.nio.chatroom;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHandler implements Runnable{


    private ChatClient client;

    public UserInputHandler(ChatClient client) {
        this.client = client;
    }

    @Override
    public void run() {


        // 输入行
        try {

            // 获取控制台输入
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = console.readLine();

                // 发送消息
                client.sendMsg(input);

                // 检查用户是否退出
                if (client.readyExit(input)) {
                    client.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
