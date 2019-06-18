package com.zsw.web.controller;

import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.CsvUtil;
import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.Role;
import com.zsw.service.RoleService;
import com.zsw.web.Result;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.CreateNewRoleCommand;
import com.zsw.web.controller.command.UpdateRoleResCommand;
import com.zsw.web.controller.representation.RoleResRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    
    private RoleService roleService;
    
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @RequestMapping(value = "/create-new-role")
    public Result  createNewRole(@RequestBody CreateNewRoleCommand command) {
        try {
            Boolean rs = roleService.createNewRole(command.getName(), command.getIcon());
        }catch (ServiceException e ) {
            return renderError(e.getMessage(), null);
        }
        return  renderSuccess("创建角色成功", null);
    }

    /**
     * 批量查找角色
     */
    @RequestMapping(value = "/find-all-role", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findAllRole(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String keyWord) {
        List<Role> roleList = null;
        try {
            
            roleList = roleService.findRoles(page, size, keyWord);
        }catch (ServiceException e) {
            e.printStackTrace();
            return renderError(e.getMessage(), null);
        }
        return renderSuccess("查询所以角色成功", roleList);
    }

    /**
     * 查询角色
     */
    @RequestMapping(value = "/find-role-by-id", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findRoleById (@RequestParam Integer id) {
        Role role = null;
        try{
            role = roleService.findRoleById(id);
        }catch (ServiceException e) {
            return  renderSuccess(e.getMessage(), null);
        }
        return  renderSuccess("查询角色成功", role);
    }

    /**
     * 删除功能不完善
     */
    @RequestMapping(value = "/remove-role-by-id", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result removeRoleById(@RequestParam Integer id) {
        boolean isSucc = false;
        try {
            isSucc = roleService.removeRoleById(id);
        }catch (ServiceException e) {
            return renderError(e.getMessage(), null);
        }
        if (!isSucc) {
            return renderError("角色删除失败", null);
        }
        return renderSuccess("角色删除成功",null);
    }

    /**
     * 更新角色
     */
    @RequestMapping(value = "/update-role", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateRoleById(@RequestBody CreateNewRoleCommand command) {
        try {
            roleService.updateRole(command);
        }catch (ServiceException e) {
            return renderError(e.getMessage(), null);
        }
        return renderSuccess("角色更新成功",null);
    }
    
    /*************************角色和关系的controller*******************************************/

    /**
     * 更新角色资源
     */
    @RequestMapping(value = "/update-role-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateRoleRes(@RequestBody UpdateRoleResCommand command) {
        try {
            roleService.updateRoleRes(command.getRoleId(), command.getResIds());
        }catch (ServiceException e) {
            return renderError(e.getMessage(), null);
        }
        return renderSuccess("更新角色的资源成功", null);
    }

    /**
     * 查询角色的资源 和 所有资源
     */
    @RequestMapping(value = "/find-role-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findRoleRes(@RequestParam Integer id) {
        Role role = null;
        try {
            role = roleService.findRoleById(id);
        }catch (ServiceException e ) {
            e.printStackTrace();
            return renderError(e.getMessage(), null);
        }
        return  renderSuccess("查询角色对应资源列表成功", role);
    }

    /**
     * 下载
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> d(HttpServletRequest request ) throws Exception {

        String filename = "resourcesInfo.csv";

        List<Role> datas = roleService.findRoles(1,999999, null);
        ByteArrayOutputStream baos= CsvUtil.process(datas);
        baos.close();

        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题  
        String downloadFielName = new String(filename.getBytes("UTF-8"),"UTF-8");
        //通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(baos.toByteArray(),
                headers, HttpStatus.CREATED);
    }
    
    
    

}
