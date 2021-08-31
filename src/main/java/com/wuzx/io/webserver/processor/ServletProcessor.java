package com.wuzx.io.webserver.processor;

import com.wuzx.io.webserver.connector.ConnectorUtils;
import com.wuzx.io.webserver.connector.Request;

import javax.servlet.Servlet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServletProcessor {

    public URLClassLoader getServletLoader() throws MalformedURLException {
        File file = new File(ConnectorUtils.WEB_ROOT);

        URL webroolUrl = file.toURI().toURL();
        return new URLClassLoader(new URL[]{webroolUrl});
    }


    public Servlet getServlet(URLClassLoader loader, Request request) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        final String uri = request.getUri();
        final String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        Class serlvetClasss = loader.loadClass(servletName);

        Servlet servlet = (Servlet) serlvetClasss.newInstance();
        return servlet;
    }
}
