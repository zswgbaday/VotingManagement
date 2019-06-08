package com.zsw.service;

import com.zsw.dao.mappers.RoleMapper;
import com.zsw.pojo.role.Role;
import com.zsw.pojo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    private RoleMapper roleMapper;
    
    @Autowired
    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    
    public Set<Role> getRolesByUser(User user) {
        Set<Role> roleSet = roleMapper.findRolesByUserId(user.getId()).stream().collect(Collectors.toSet());
        user.setRole(roleSet);
        return roleSet;
    }

    /**
     *  暂时不做，没有合理区分角色和资源，导致不好创建新的角色，反正和投票管理系统关系不大，以后再说 
     */
    public Role createNewRole(Integer id, String name, String icon) {
//        roleMapper.insertNewRole
        return null;
    }

    public Integer removeRelationUserByUserId(String userId) {
        return roleMapper.deleteRelationUserByUserId(userId);
    }
}
