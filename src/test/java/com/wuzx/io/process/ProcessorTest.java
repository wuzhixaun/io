package com.wuzx.io.process;

import com.wuzx.io.webserver.connector.Request;
import com.wuzx.io.webserver.processor.ServletProcessor;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.Servlet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLClassLoader;

public class ProcessorTest {

    private static final String VALID_REQUEST_URI = "GET /servlet/TimeServlet HTTP/1.1";
    @Test
    public void givenServletRequest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        InputStream inputStream = new ByteArrayInputStream(VALID_REQUEST_URI.getBytes());
        Request request = new Request(inputStream);
        request.parse();
        ServletProcessor servletProcessor = new ServletProcessor();
        final URLClassLoader servletLoader = servletProcessor.getServletLoader();
        final Servlet servlet = servletProcessor.getServlet(servletLoader, request);

        Assert.assertEquals("TimeServlet", servlet.getClass().getName());
    }
}
