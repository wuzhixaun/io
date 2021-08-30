package com.wuzx.io.connector;

import com.wuzx.io.webserver.Request;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RequestTest {

    private static final String VALID_REQUEST_URI = "GET /index.html HTTP/1.1";

    @Test
    public void givenValidRequest() {
        InputStream inputStream = new ByteArrayInputStream(VALID_REQUEST_URI.getBytes());
        Request request = new Request(inputStream);
        request.parse();
        Assert.assertEquals("/index.html", request.getUri());
    }
}
