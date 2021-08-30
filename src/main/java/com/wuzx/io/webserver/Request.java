package com.wuzx.io.webserver;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private static final int BUFFER_SIZE = 1024;
    private InputStream inputStream;
    private String uri;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getUri() {
        return uri;
    }

    public void parse() {
        int lenth = 0;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            lenth = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder request = new StringBuilder();
        for (int i = 0; i < lenth; i++) {
            request.append((char) buffer[i]);
        }
        uri = parseUri(request.toString());
    }

    private String parseUri(String request) {
        int index1, index2;
        index1 = request.indexOf(' ');
        if (index1 != -1) {
            index2 = request.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return request.substring(index1 + 1, index2);
            }
        }

        return "";
    }
}
