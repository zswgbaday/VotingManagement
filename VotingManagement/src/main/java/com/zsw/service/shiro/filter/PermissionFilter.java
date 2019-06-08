package com.zsw.service.shiro.filter;


import com.zsw.common.util.JSONUtil;
import com.zsw.web.Result;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PermissionFilter extends AccessControlFilter { 

    private Logger log = LoggerFactory.getLogger(PermissionFilter.class);


    /**
     * 判断是否允许访问资源
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {

        Subject subject = getSubject(request, response);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.debug("开始判断用户{}是否可访问资源{}", "someone", httpRequest.getRequestURI());

        //超级管理员角色
        if (subject.hasRole("superAdmin")) {
            return Boolean.TRUE;
        }

        String uri = httpRequest.getRequestURI();
        //一般用户验证
        if (subject.isPermitted(uri)) {
            return Boolean.TRUE;
        }
        if (ShiroFilterUtil.isAjax(request)) {                  //Ajax请求就返回json
            log.debug("当前用户没有权限访问，并且是Ajax请求！");

            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("当前用户没有权限访问！");

            JSONUtil.out(response, result);
        }
        return Boolean.FALSE;
    }
    /**
     * 如果被拒绝访问，调用此方法
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (null == subject.getPrincipal()) {
            saveRequest(servletRequest);
            WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.LOGIN_URL);
        }else {
            if (StringUtils.hasText(ShiroFilterUtil.UNAUTHORIZED)) {//如果有未授权页面跳转过去
                WebUtils.issueRedirect(servletRequest, servletResponse, ShiroFilterUtil.UNAUTHORIZED);
            } else {//否则返回401未授权状态码
                WebUtils.toHttp(servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return Boolean.FALSE;
    }
}
