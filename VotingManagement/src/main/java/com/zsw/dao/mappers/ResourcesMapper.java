package com.zsw.dao.mappers;

import com.zsw.pojo.role.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ResourcesMapper {
    
    Set<Resources> findAllResources();
    
    List<Resources> findResourcesByRoleId(Integer roleId);

    List<Resources> findResourcesByRoleIds(List<Integer> roleIds);
    
    
}
