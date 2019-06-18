package com.zsw.web.controller.representation;

import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.Role;

import java.util.List;

/**
 * 
 */
public class RoleResRepresentation {
    
    //所有的资源列表
    private List<Resources> resList;
    //某个角色对应的所有资源
    private Role role;

    public List<Resources> getResList() {
        return resList;
    }

    public void setResList(List<Resources> resList) {
        this.resList = resList;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
