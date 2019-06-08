package com.zsw.web.controller;

import com.zsw.common.custom.Test;
import com.zsw.dao.mappers.RoleMapper;
import com.zsw.dao.mappers.UserMapper;
import com.zsw.pojo.role.Role;
import com.zsw.pojo.user.User;
import com.zsw.service.UserService;
import com.zsw.web.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
    
    private UserService userService;
    private UserMapper userMapper;
    
    @Autowired
    public TestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    @RequestMapping("find-all-role")
    @ResponseBody
    public Set<User> findAllRole(){
        return userService.getAllUser();
    }

    @RequestMapping("test")
    @ResponseBody
    public Object test(){
        insertUser();
        return null;
    }
    public void insertUser(){
        User user = new User();
        user.createNewId();
        user.setName("user1" + Thread.currentThread());
        user.setPassword("password");
        user.setEmail(user.getName());
        HashSet<Role> set = new HashSet<Role>();
        set.add(new Role(4, "普通用户", null, null));
        user.setRole(set);

        userMapper.insertNewUser(user);
    }
}
