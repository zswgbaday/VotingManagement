package com.zsw.dao.mappers;

import com.zsw.pojo.role.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleMapper {

    @Select("select r.* from v_resources r inner join v_user u on r.role_id = u.role_id where u.id = #{userId} ")
    List<Role> findRoleByUserId(@Param("userId") String userId);
    
    Role findRoleByRoleId(@Param("roleId") Integer roleId);
    
    List<Role> findRolesByUserId(String userId);
    
    @Select("select * from v_resources")
    List<Role> findAll();

    Integer deleteRelationUserByUserId(String userId);
}
