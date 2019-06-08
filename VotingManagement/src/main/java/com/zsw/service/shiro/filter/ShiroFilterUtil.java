package com.zsw.service.shiro.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class ShiroFilterUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ShiroFilterUtil.class);

    //登录页面
    static final String LOGIN_URL = "/login";

    //没有权限提醒
    final static String UNAUTHORIZED = "/unauth";

    //session attribute 名字
    public static final String STATUS_ONLINE = ShiroFilterUtil.class.getCanonicalName() + "_online";

    //session状态
    public static final String STATUS_SESSION_KEY = ShiroFilterUtil.class.getCanonicalName() + "_online_status";
    public static final String STATUS_KICK = ShiroFilterUtil.class.getCanonicalName() + "_kick";
    public static final String STATUS_ABORT = ShiroFilterUtil.class.getCanonicalName() + "_abort";

    /**
     * 是否是Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

}
