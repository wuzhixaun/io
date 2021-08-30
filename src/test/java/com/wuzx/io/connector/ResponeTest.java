package com.wuzx.io.connector;

import com.wuzx.io.webserver.ConnectorUtils;
import com.wuzx.io.webserver.Request;
import com.wuzx.io.webserver.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponeTest {
    private static final String VALID_REQUEST_URI = "GET /index.html HTTP/1.1";
    private static final String IN_VALID_REQUEST_URI = "GET /4041.html HTTP/1.1";


    private static final String status_200 = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String status_404 = "HTTP/1.1 404 file not found\r\n\r\n";

    @Test
    public void givenValidRequest() throws IOException {
        // 创建request
        InputStream inputStream = new ByteArrayInputStream(VALID_REQUEST_URI.getBytes());
        Request request = new Request(inputStream);
        request.parse();

        OutputStream out = new ByteArrayOutputStream();


        Response respone = new Response(out, request);

        respone.sendStaticResoure();


        String res = readFiletoString(ConnectorUtils.WEB_ROOT + "/index.html");
        Assert.assertEquals(status_200 + res, out.toString());
    }


    @Test
    public void givenInValidRequest() throws IOException {
// 创建request
        InputStream inputStream = new ByteArrayInputStream(IN_VALID_REQUEST_URI.getBytes());
        Request request = new Request(inputStream);
        request.parse();

        OutputStream out = new ByteArrayOutputStream();


        Response respone = new Response(out, request);

        respone.sendStaticResoure();


        String res = readFiletoString(ConnectorUtils.WEB_ROOT + "/404.html");
        Assert.assertEquals(status_404 + res, out.toString());
    }

    private static String readFiletoString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
