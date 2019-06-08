package com.zsw.service;

import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.Encrypt;
import com.zsw.common.util.Uuid;
import com.zsw.dao.mappers.UserMapper;
import com.zsw.pojo.user.User;
import com.zsw.web.controller.command.FindUsersByRuleCommand;
import com.zsw.web.controller.command.UpdateUserCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(UserService.class);

    private UserMapper userMapper;
    private RoleService roleService;

    @Autowired
    private UserService(UserMapper userMapper, RoleService roleService) {
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    public Set<User> getAllUser() {
        List<User> userList = userMapper.selectAllUser();
        //为user赋予权限属性
        userList.stream().forEach(user -> {
            roleService.getRolesByUser(user);
        });
        return userList.stream().collect(Collectors.toSet());
    }

    /**
     * 新建用户
     */
    public User createNewUser(String username, String password, String email, Integer age, String address, String phone) {
        User user = new User(Uuid.getUUID32(), username, password, phone, age, address, email, null, null);
        Integer rs = userMapper.insertNewUser(user);
        if (rs != 0) {
            return user;
        }else {
//            throw new ServiceException("新增用户失败");
            return null;
        }
    }

    /**
     * 更新用户
     */
    public Boolean updateUser(UpdateUserCommand comand) {
        log.debug("更新用户:{}" + comand);
        User user = (User) comand;
        if (StringUtils.isBlank(user.getId()) ||
                StringUtils.isBlank(user.getEmail()) ||
                StringUtils.isBlank(user.getName())) {
            throw new ServiceException("邮箱|用户名 不能为空");
        }
        if (comand.getOldPassword() != null && ! StringUtils.isBlank(comand.getOldPassword())) {
            String password = userMapper.getUserById(comand.getId()).getPassword();
            if (password.equals(Encrypt.MD5(comand.getOldPassword()))) {
                comand.setPassword(Encrypt.MD5(comand.getNewPassword()));
            }else {
                throw new ServiceException("旧密码错误");
            }
        }else {
            user.setPassword(null);
        }
        int rs = userMapper.updateUserById(user);
        if (rs != 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean removeUser(String userId) {
        log.debug("删除用户:{}", userId);
        if (userMapper.deleteUserById(userId) != 0) {
            if (roleService.removeRelationUserByUserId(userId) != 0) {
                return Boolean.TRUE;
            } else {
                throw new ServiceException("级联删除用户-角色失败!");
            }
        } else {
//            throw new ServiceException("删除用户"+ userId +"失败");
            return Boolean.FALSE;
        }
    }

    public List<User> findUsersWithRule(FindUsersByRuleCommand command) {
       return userMapper.selectUsersWithRule(command);
    }

    public User getUserByEmail(String loginEmail) {
        if (StringUtils.isBlank(loginEmail)) {
            throw new ServiceException("用户邮箱不能为空");
        }
        return userMapper.getUserByEmail(loginEmail);
    }

    public User getUserById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("用户id不能为空");
        }
        return userMapper.getUserById(id);
    }

    public boolean setPhotoById(String id, byte[] bytes) {
        if (userMapper.setPhotoById(id, bytes) > 0) {
            return true;
        }
        return false;
    }
    
    public byte[] getPhotoById(String id) throws IOException {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("用户id不能为空");
        }
        User user = userMapper.getPhotoById(id);
        return user.getPhoto();
    }
}
