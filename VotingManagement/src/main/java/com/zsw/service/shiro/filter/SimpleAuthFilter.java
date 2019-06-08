package com.zsw.service.shiro.filter;

import com.zsw.common.util.JSONUtil;
import com.zsw.web.Result;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登录filter
 * Created by Administrator on 2019-04-07.
 */
public class SimpleAuthFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        boolean flag = false;
        if (subject.isAuthenticated()) {
            //已经验证登录
            flag = true;

        } else {
            if (subject.getPrincipal() != null) {
                //使用了记住我功能

                //提示验证超时
                /*
                try {
                    Session session = subject.getSession(false);
                    session.getStartTimestamp();
                } catch (ExpiredSessionException e) {
                    // 超时

                }
                */
            }
        }
        return flag;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (ShiroFilterUtil.isAjax(request)) {
            //返回ajax
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("请登录后操作");
            JSONUtil.outWithStatus(response, result, HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            saveRequestAndRedirectToLogin(request, response);
        }
        return false;
    }
}
