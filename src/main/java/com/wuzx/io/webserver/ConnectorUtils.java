package com.wuzx.io.webserver;

import java.io.File;

public class ConnectorUtils {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private static final String PROTOCOL = "HTTP/1.1";
    private static final String CARRIAGE = "\r";
    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";

    public static String renderStatus(HttpStatus httpStatus) {
        StringBuilder sb = new StringBuilder(PROTOCOL)
                .append(SPACE)
                .append(httpStatus.getCode())
                .append(SPACE)
                .append(httpStatus.getReason())
                .append(CARRIAGE).append(NEWLINE)
                .append(CARRIAGE).append(NEWLINE);
        return sb.toString();
    }





}
