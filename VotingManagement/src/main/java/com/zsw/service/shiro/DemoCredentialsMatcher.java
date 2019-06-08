package com.zsw.service.shiro;

import com.zsw.common.util.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class DemoCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 密码比较的规则
     * token:用户在界面输入的用户名和密码
     * info: 从数据库中得到的加密数据
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户输入的密码，并加密
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String md5Pwd = Encrypt.MD5(new String(upToken.getPassword()));

        // 获取数据库中的加密密码
        String pwd = (String) info.getCredentials();

        // 返回比较结果
        return this.equals(md5Pwd, pwd);
    }

}
