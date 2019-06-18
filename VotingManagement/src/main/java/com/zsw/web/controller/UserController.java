package com.zsw.web.controller;

import com.csvreader.CsvWriter;
import com.zsw.common.enums.RoleType;
import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.CsvUtil;
import com.zsw.common.util.JSONUtil;
import com.zsw.pojo.user.User;
import com.zsw.service.UserService;
import com.zsw.web.Result;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.CreateNewUserCommand;
import com.zsw.web.controller.command.FindUsersByRuleCommand;
import com.zsw.web.controller.command.PhotoCommand;
import com.zsw.web.controller.command.UpdateUserCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.IMAGE_JPEG;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    
    private UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @RequestMapping(value = "/create-new-user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result createNewUser(@RequestBody CreateNewUserCommand command){
        User user = userService.createNewUser(
                command.getUsername(),command.getPassword(),command.getEmail(), command.getAge(),command.getAddress(),command.getPhone());
        Result rs = new Result();
        if (user != null) {
            rs.setSuccess(true);
            rs.setMsg("新建用户" + user.getName() + "成功");
            rs.setObj(user);
        }else {
            rs.setSuccess(false);
            rs.setMsg("新建用户" + user.getName() + "失败,  请重试");
        }
        return rs;
    }

    @RequestMapping(value = "/update-user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result createNewUser(@RequestBody UpdateUserCommand command) {
        Result rs = new Result();
        try {
            if (userService.updateUser(command)) {
                rs.setSuccess(true);
                rs.setMsg("更新用户" + command.getName() + "成功");
                rs.setObj(command);
                return rs;
            } else {
                rs.setSuccess(false);
                rs.setMsg("更新用户" + command.getName() + "失败, 请稍后重试");
                return rs;
            }
        }catch (ServiceException e) {
            return renderError(e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/delete-user", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result deleteUser(@RequestParam("id") String userId){
        Result rs = new Result();
        if (StringUtils.isBlank(userId)) {
            rs.setSuccess(false);
            rs.setMsg("获取用户id失败,请重新尝试");
            return rs;
        }
        if (userService.removeUser(userId)){
            rs.setSuccess(true);
            rs.setMsg("删除用户成功");
        }else {
            rs.setSuccess(false);
            rs.setMsg("删除用户失败");
        }
        return rs;
    }

    @RequestMapping(value = "/select-user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result selectUserWithRule(@RequestBody FindUsersByRuleCommand command) {
        if (command.getPage() == null) {
            command.setPage(1);
        }
        if (command.getSize() == null) {
            command.setSize(20);
        }
        List<User> users = userService.findUsersWithRule(command);
        Result rs = new Result(true, "查询成功", users);
        return rs;
    }

    @RequestMapping(value = "/find-user-by-id", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findUserById (@RequestParam("id") String id) {
        User user = null;
        try {
            user = userService.getUserById(id);
        }catch (ServiceException e) {
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("查询用户信息成功", user);
    }

    /**
     * 获取当前登录用户的信息
     */
    @RequestMapping(value = "/get-user-info", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findUser () {
        User login = (User) SecurityUtils.getSubject().getPrincipal();
        SecurityUtils.getSubject().hasRole(RoleType.SUPERADMIN.name());
        if (login == null) {
            return renderError("用户未登录", null);
        }
        User user = null;
        try {
            user = userService.getUserById(login.getId());
        }catch (ServiceException e) {
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("查询登录用户信息成功", user);
    }

    /**
     * 下载用户信息
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> d(HttpServletRequest request ) throws Exception {

        String filename = "userInfo.csv";

        List<User> datas = userService.getAllUser().stream().collect(Collectors.toList());
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

    /**
     * 上传用户头像
     */
    @RequestMapping(value = "/upload-photo")
    @ResponseBody
    public Result upLoad (PhotoCommand command) {
        MultipartFile file = command.getPhoto();
        Object obj = SecurityUtils.getSubject().getPrincipal();
        if (obj == null) {
            renderError("用户未登录", null);
        }
        String userId = ((User) obj).getId();
        try {
            InputStream in =  file.getInputStream();
            byte[] bytes = new byte[(int)command.getPhoto().getSize()];
            in.read(bytes);
            if (! userService.setPhotoById(userId, bytes)) {
                return renderError("持久化图片失败，请重试", null);
            }
        }catch (IOException e) {
            return renderError("上传图片错误，请重试", null);
        }
        
        return renderSuccess("上传成功", null);
    }

    /**
     * 下载用户头像
     */
    @RequestMapping(value = "/show-photo")
    public ResponseEntity<byte[]> showPhoto (HttpServletResponse resp,
                                             @RequestParam(value = "id", required = false) String id) throws IOException {
        byte[] photo = null;
        if (StringUtils.isBlank(id)) {
            Object obj = SecurityUtils.getSubject().getPrincipal();
            if (obj == null) {
                return new ResponseEntity<byte[]>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            String userId = ((User) obj).getId();
            photo = userService.getPhotoById(userId);
        }else {
            photo = userService.getPhotoById(id);
        }
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(photo, httpHeaders, HttpStatus.CREATED);
    }


}
