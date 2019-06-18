package com.zsw.dao.mappers;

import com.zsw.pojo.role.Resources;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ResourcesMapper {
    
    Set<Resources> findAllResources();
    
    List<Resources> findResourcesByRoleId(Integer roleId);

    List<Resources> findResourcesByRoleIds(@Param("roleIds") List<Integer> roleIds);


    Integer insertNewRes(Resources res);
    
    Integer updateRes(Resources res);
    
    Integer deleteRes(Integer id);

    Resources findResById(Integer id);

    List<Resources> findResourcesByKeyWord(String keyWord);
}
