package com.zsw.web.controller;

import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.CsvUtil;
import com.zsw.pojo.role.Resources;
import com.zsw.pojo.vote.Vote;
import com.zsw.service.ResourcesService;
import com.zsw.web.Result;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.CreateNewResCommand;
import com.zsw.web.controller.command.FindVoteCommand;
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
@RequestMapping("/res")
public class ResourceController extends BaseController {
    
    private ResourcesService resourcesService;
    
    @Autowired
    public ResourceController (ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    /***************ResourcesController*****************************/

    /**
     * 查询所有资源
     */
    @RequestMapping(value = "/find-all-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findAllRes() {
        List<Resources> list = null;
        try {
            list = resourcesService.getAllRes();
        }catch (ServiceException e){
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("查询所有资源列表成功", list);
    }

    /**
     * 查询资源
     */
    @RequestMapping(value = "/find-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findRes(@RequestParam String keyWord) {
        List<Resources> list = null;
        try {
            list = resourcesService.getResByKeyWord(keyWord);
        }catch (ServiceException e){
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("查询所有资源列表成功", list);
    }

    /**
     * 查询某个res
     */
    @RequestMapping(value = "/find-res-by-id", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findResById(@RequestParam Integer id) {
        Resources res = new Resources();
        try {
            res = resourcesService.getResById(id);
        }catch (ServiceException e) {
            return renderError(e.getMessage(), null);
        }
        return renderSuccess("查询资源成功", res);
    }
    
    /**
     * 新建资源
     */
    @RequestMapping(value = "/create-new-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result createNewRes(@RequestBody CreateNewResCommand command) {
        boolean isSucc = false;
        try {
            isSucc = resourcesService.createNewRes(command);
        }catch (ServiceException e){
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("新增资源成功", null);
    }

    /**
     * 更新资源
     */
    @RequestMapping(value = "/update-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateRes(@RequestBody CreateNewResCommand command) {
        boolean isSucc = false;
        try {
            isSucc = resourcesService.updateRes(command);
        }catch (ServiceException e){
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("修改资源成功", null);
    }
    
    /**
     * 删除资源
     */
    @RequestMapping(value = "/remove-res", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findAllRes(@RequestParam Integer id) {
        boolean isSucc = false;
        try {
            isSucc = resourcesService.removeRes(id);
        }catch (ServiceException e){
            e.printStackTrace();
            return  renderError(e.getMessage(), null);
        }
        return  renderSuccess("删除资源成功", null);
    }


    /**
     * 下载
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> d(HttpServletRequest request ) throws Exception {

        String filename = "resourcesInfo.csv";

        List<Resources> datas = resourcesService.getAllRes();
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
