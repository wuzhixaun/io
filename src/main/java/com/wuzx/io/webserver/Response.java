package com.wuzx.io.webserver;

import java.io.*;

public class Response {


    private static final int BUFFER_SIZE = 1024;
    private OutputStream out;
    private Request request;

    public Response(OutputStream out, Request request) {
        this.out = out;
        this.request = request;
    }

    public void sendStaticResoure() throws IOException {
        File file = new File(ConnectorUtils.WEB_ROOT, request.getUri());
        try {
            write(file, HttpStatus.SC_OK);
        } catch (IOException e) {
            write(new File(ConnectorUtils.WEB_ROOT, "404.html"), HttpStatus.SC_NOT_FOUND);
        }
    }

    public void write(File resource, HttpStatus httpStatus) throws IOException {
        try (FileInputStream fis = new FileInputStream(resource)) {
            out.write(ConnectorUtils.renderStatus(httpStatus).getBytes());

            // 将资源文件实际内容写进去
            byte[] buffer = new byte[BUFFER_SIZE];
            int lengt = 0;

            while ((lengt = fis.read(buffer)) != -1) {
                out.write(buffer, 0, lengt);
            }

        }
    }
}


