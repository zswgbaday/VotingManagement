package com.zsw.dao.mappers;

import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.RoleRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleResMapper {
    
    List<RoleRes> findByRoleId(Integer id);
    
    List<RoleRes> findByResId(Integer id);
    
    Integer delete(@Param("roleId") Integer roleId, @Param("resId") Integer resId);
    
    Integer insert(@Param("roleId") Integer roleId, @Param("resId") Integer resId);
    
    
}
