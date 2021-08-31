package com.wuzx.io.webserver.connector;

/**
 * http返回状态码
 *
 */
public enum HttpStatus {
    SC_OK(200, "OK"),
    SC_NOT_FOUND(404, "file not found"),

    ;

    private int code;
    private String reason;

    HttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }


    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
