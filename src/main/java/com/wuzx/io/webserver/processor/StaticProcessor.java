package com.wuzx.io.webserver.processor;

import com.wuzx.io.webserver.connector.Request;
import com.wuzx.io.webserver.connector.Response;

import java.io.IOException;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName StaticProcessor.java
 * @Description TODO
 * @createTime 2021年08月31日 10:01:00
 */
public class StaticProcessor {


    public void process(Request request, Response response) throws IOException {
        response.sendStaticResoure();
    }
}
