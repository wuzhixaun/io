package com.wuzx.io.webserver.connector;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {


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

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(out, true);
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}


