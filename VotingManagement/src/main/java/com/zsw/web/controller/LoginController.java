package com.zsw.web.controller;

import com.zsw.common.util.Encrypt;
import com.zsw.pojo.user.User;
import com.zsw.web.Result;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.CreateNewVoteCommand;
import com.zsw.web.controller.command.DoLoginCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController {

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String toLoginPage(){
        return "redirect:/views/login/login.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object doLogin(@RequestBody DoLoginCommand command) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(command.getLoginEmail(), Encrypt.MD5(command.getPassword())); //md5加密应该放在前台吧...
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            log.error("账号不存在：{}", e);
            return renderError("账号不存在", null);
        } catch (DisabledAccountException e) {
            log.error("账号未启用：{}", e);
            return renderError("账号未启用", null);
        } catch (IncorrectCredentialsException e) {
            log.error("密码错误：{}", e);
            return renderError("密码错误", null);
        } catch (RuntimeException e) {
            log.error("未知错误,请联系管理员：{}", e);
            return renderError("未知错误,请联系管理员", null);
        }

        Result result = new Result();
        result.setSuccess(true);
        result.setMsg("登录成功");

        //跳转地址
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String url = null;
        if (null != savedRequest) {
            url = savedRequest.getRequestUrl();
        }
        //默认登录成功后跳转到
        if (StringUtils.isBlank(url)) {
            url = request.getContextPath() + "/index";
        }

        //单点登录，同时在线的用户只能有一个,什么鬼，不做了
        
        //返回json
        result.setUri(url);
        return result;
    }

    /**
     * 提示无权限页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    public String nuAuthPage(){
        return "/unauth/unauth";
    }

    /**
     * 主页
     */
    @RequestMapping(value = "index")
    public String toIndex() {
        return rediPage("views/index/index.html");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return rediPage("login/register.html");
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String doLogin(@RequestBody CreateNewVoteCommand command) {
        return forwPage("user/create-new-user");
    }

    /**
     * 退出
     *
     * @return {Result}
     */
    @RequestMapping(value = "/logout", produces = "application/json; charset=utf-8")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        boolean isUser = subject.getPrincipal() instanceof User;
        subject.logout();
        return rediPage("login");
    }
    
    @RequestMapping(value = "/get-user-info", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result getUserInfo () {
        Subject login = SecurityUtils.getSubject();
        if (login.getPrincipal() == null) {
            return renderError("请登录", null);
        }
        User user = (User) login.getPrincipal();
        return  renderSuccess("", user);
    }
    
    @RequestMapping("check-login")
    public String checkLogin () {
        Object obj = SecurityUtils.getSubject().getPrincipal();
        if (obj == null) {
            return rediPage("views/login/login.html");
        }
        return rediPage("views/user/home.html");
    }
}
