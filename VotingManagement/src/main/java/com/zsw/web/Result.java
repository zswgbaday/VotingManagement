package com.zsw.web;

/**
 * 专为响应ajax请求准备的类
 */
public class Result {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private static final long serialVersionUID = 5576237395711742681L;

    
    private boolean success = false;
    private String msg = "";
    private Object obj = null;
    private String uri = "";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Result(boolean success, String msg, Object obj) {
        this.success = success;
        this.msg = msg;
        this.obj = obj;
    }

    public Result(boolean success, String msg, Object obj, String uri) {
        this.success = success;
        this.msg = msg;
        this.obj = obj;
        this.uri = uri;
    }

    public Result() {
    }
}
