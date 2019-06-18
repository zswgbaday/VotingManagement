package com.zsw.dao.mappers;

import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.RoleRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserRoleMapper {

    List<RoleRes> findByRoleId(Integer id);

    List<RoleRes> findByUserId(String id);

    Integer delete(@Param("roleId") Integer roleId, @Param("userId") String userId);

    Integer insert(@Param("roleId") Integer roleId, @Param("userId") String userId);
    
    
}
