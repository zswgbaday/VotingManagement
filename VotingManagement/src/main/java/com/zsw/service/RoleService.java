package com.zsw.service;

import com.zsw.common.exception.ServiceException;
import com.zsw.dao.mappers.RoleMapper;
import com.zsw.dao.mappers.RoleResMapper;
import com.zsw.dao.mappers.UserRoleMapper;
import com.zsw.pojo.role.Role;
import com.zsw.pojo.role.RoleRes;
import com.zsw.pojo.user.User;
import com.zsw.web.controller.command.CreateNewRoleCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    private RoleMapper roleMapper;
    private UserRoleMapper userRoleMapper;
    private RoleResMapper roleResMapper;
    
    @Autowired
    public RoleService(RoleMapper roleMapper, UserRoleMapper userRoleMapper, RoleResMapper roleResMapper) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.roleResMapper = roleResMapper;
    }
    
    public Set<Role> getRolesByUser(User user) {
        Set<Role> roleSet = roleMapper.findRolesByUserId(user.getId()).stream().collect(Collectors.toSet());
        user.setRole(roleSet);
        return roleSet;
    }

    /**
     *  暂时不做，没有合理区分角色和资源，导致不好创建新的角色，反正和投票管理系统关系不大，以后再说 
     */
    public Boolean createNewRole(String name, String icon) {
        if (StringUtils.isBlank(name)){
            throw new ServiceException("新增角色名称不能为空");
        }
        Role role = new Role();
        role.setName(name);
        role.setIcon(icon);
        if (roleMapper.insertNewRole(role) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean UpdateRole(Integer id, String name, String icon) {
        if (id == null) {
            throw new ServiceException("更新角色id不能为空");
        }
        if (StringUtils.isBlank(name)){
            throw new ServiceException("更新角色名称不能为空");
        }
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setIcon(icon);
        if (roleMapper.updateRole(role) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据用户id，删除用户角色关系表里的关系
     * @param userId
     * @return
     */
    public Integer removeRelationUserByUserId(String userId) {
        return roleMapper.deleteRelationUserByUserId(userId);
    }

    /**
     * 根据id查询角色
     */
    public Role findRoleById(Integer id) {
        if (id == null) {
            throw new ServiceException("角色id不能为空");
        }
        Role role = roleMapper.findRoleByRoleId(id);
        return role;
    }
    
    /**
     * 逻辑判断，然后删除角色
     * @param id
     */
    public boolean removeRoleById(Integer id) {
        if(id == null) {
            throw new ServiceException("角色id不能为空");
        }
        if (roleResMapper.findByRoleId(id).size() != 0){
            throw new ServiceException("角色仍有关联的资源，无法删除角色");
        }
        if (userRoleMapper.findByRoleId(id).size() != 0){
            throw new ServiceException("角色仍有关联的用户，无法删除角色");
        }
        if (roleMapper.deletRole(id) < 1) {
            return false;
        }
        return true;
    }

    /**
     * 更新角色对应的资源
     * @param roleId
     * @param resIds
     */
    public boolean updateRoleRes(Integer roleId, List<Integer> resIds) {
        if(roleId == null) {
            throw new ServiceException("角色id不能为空");
        }
//        List<RoleRes> roleResOld = roleResMapper.findByRoleId(roleId);
        try{
            roleResMapper.delete(roleId, null);
            for (Integer res : resIds) {
                roleResMapper.insert(roleId, res);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return true;
    }

    /**
     * 查询角色对应的所有资源列表
     */
    public Role findRoleRes (Integer id) {
        Role role = null;
        try {
            role = roleMapper.findRoleByRoleId(id);
        }catch (RuntimeException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return role;
    }

    public boolean updateRole(CreateNewRoleCommand command) {
        if (command.getIcon() == null) {
            throw new ServiceException("更新角色id不能为空");
        }
        boolean isSucc = false;
        try{
            Role role = new Role();
            role.setId(command.getId());
            role.setName(command.getName());
            role.setIcon(command.getIcon());
            roleMapper.updateRole(role);
        }catch (RuntimeException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return true;
    }

    //查询所有role
    public List<Role> findRoles(Integer page, Integer size, String keyWord) {
        List<Role> roleList = null;
        if (page == null || size == null) {
            throw new ServiceException("页码和大小不能为空");
        }
        if (StringUtils.isBlank(keyWord)) {
            keyWord = null;
        }
        roleList = roleMapper.findRoles(page, size, keyWord);
        return roleList;
    }

    public boolean insertUserRole(String id, int roleId) {
        int rs = userRoleMapper.insert(roleId, id);
        if (rs != 0) {
            return true;
        }
        return false;
    }
}
