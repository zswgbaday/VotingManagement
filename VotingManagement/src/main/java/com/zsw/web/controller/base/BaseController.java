package com.zsw.web.controller.base;

import com.zsw.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public abstract class BaseController implements Serializable {

    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获得当前url
     *
     * @return
     */
    protected String makeUrl() {
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }


    /**
     * ajax失败
     * @param msg 失败的消息
     * @return {Object}
     */
    protected Result renderError(String msg, Object obj) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMsg(msg);
        result.setObj(obj);
        return result;
    }

    /**
     * ajax成功
     * @param msg 消息
     * @return {Object}
     */
    protected Result renderSuccess(String msg, Object obj) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMsg(msg);
        result.setObj(obj);
        return result;
    }

    /**
     * 创建一个默认ajax返回对象
     * @return
     */
    protected Result renderResult(){
        //其它功能待添加
        return new Result();
    }
    /**
     * 获得上下文路径
     * @return
     */
    protected String getContextPath(){
        return request.getContextPath();
    }

    /**
     * 重定向到页面
     */
    protected String rediPage(String page) {
        return "redirect:/" + page;
    }

    protected String forwPage(String page) {
        return "forward:/" + page;
    }
}
