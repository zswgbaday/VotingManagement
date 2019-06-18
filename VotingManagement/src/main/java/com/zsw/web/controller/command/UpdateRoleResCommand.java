package com.zsw.web.controller.command;

import java.util.List;

public class UpdateRoleResCommand {
    
    private Integer roleId;
    private List<Integer> resIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getResIds() {
        return resIds;
    }

    public void setResIds(List<Integer> resIds) {
        this.resIds = resIds;
    }
}
