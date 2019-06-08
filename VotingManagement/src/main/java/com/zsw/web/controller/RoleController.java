package com.zsw.web.controller;

import com.zsw.pojo.role.Role;
import com.zsw.service.RoleService;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.CreateNewRoleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    
    private RoleService roleService;
    
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @RequestMapping(value = "create-new-role")
    public Role createNewRole(CreateNewRoleCommand command) {
        return roleService.createNewRole(command.getId(), command.getName(), command.getIcon());
    }
}
