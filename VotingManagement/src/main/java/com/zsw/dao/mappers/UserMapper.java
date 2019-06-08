package com.zsw.dao.mappers;

import com.zsw.pojo.user.User;
import com.zsw.web.controller.command.FindUsersByRuleCommand;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {

    /** 得到所有用户，没有权限 */
    List<User> selectAllUser();


    List<User> getUserListByUsername(@Param("username") String username);
    
    User getUserByEmail(String loginEmail);

    Integer insertNewUser(User user);

    Integer updateUserById(User user);

    Integer deleteUserById(String userId);

    List<User> selectUsersWithRule(FindUsersByRuleCommand command);

    User getUserById(String id);

    Integer setPhotoById(@Param("id") String id, @Param("photo") byte[] bytes);

    User getPhotoById(String id);
}
