package com.zsw.web.controller;

import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.CsvUtil;
import com.zsw.pojo.user.User;
import com.zsw.pojo.vote.Vote;
import com.zsw.service.VoteService;
import com.zsw.web.Result;
import com.zsw.web.controller.base.BaseController;
import com.zsw.web.controller.command.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vote")
public class VoteController extends BaseController {
    
    private VoteService voteService;
    
    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }
    
    @RequestMapping(value = "/create-new-vote", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result createNewVote(@RequestBody CreateNewVoteCommand command) throws IOException {
        Result rs = new Result();
        
        Subject sub = SecurityUtils.getSubject();
        User loginUser = (User) sub.getPrincipal();
        //测试用
        User testAdmin = new User();
        testAdmin.setId("0");
        testAdmin.setName("testAdmin");
        
        Vote vote = null;
        try {
            vote = voteService.insertNewVote(command , loginUser == null ? testAdmin : loginUser);
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("新增投票成功");
        rs.setObj(vote);
        return rs;
    }
    
    @RequestMapping(value = "upload-photo")
    @ResponseBody
    public Result upLoadPhoto (MultipartFile photo, String id) {
//    public Result upLoadPhoto (UpLoadVoteImageCommand command) {
//        MultipartFile photo = command.getPhoto();
//        String id = command.getId();
        try {
            InputStream in =  photo.getInputStream();
            byte[] bytes = new byte[(int)photo.getSize()];
            in.read(bytes);
            
            if (! voteService.upLoadPhoto(id, bytes)) {
                return renderError("持久化图片失败，请重试", null);
            }
        } catch (IOException e) {
            return renderError("上传图片错误，请重试", e.getMessage());
        }
        return renderSuccess("上传成功", null);
    }

    /**
     * 下载投票图片
     */
    @RequestMapping(value = "/show-photo/{id}")
    public ResponseEntity<byte[]> showPhoto (HttpServletResponse resp, @PathVariable("id") String id ) throws IOException {
        if (StringUtils.isBlank(id)) {
            return new ResponseEntity<byte[]>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        byte[] photo = voteService.getPhotoById(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(photo, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update-vote", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateVote(@RequestBody UpdateVoteCommand command) {
        Result rs = new Result();

        Vote vote = null;
        try {
            vote = voteService.updateVoteStatus(command.getId(), command.getStatus(), command.getStopTime(), command.getTitle());
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("更新投票成功");
        rs.setObj(vote);
        return rs;
    }

    @RequestMapping(value = "/delete-vote", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result deleteVote(@RequestParam String id) {
        try {
            voteService.removeVoteById(id);
        }catch (ServiceException e) {
            return renderError(e.getMessage(),null);
        }
        return renderSuccess("删除投票成功", null);
    }

    @RequestMapping(value = "/do-vote", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result updateVote(@RequestBody UpdateVoteResultComand command) {
        Result rs = new Result();

        Vote vote = null;
        try {
            vote = voteService.updateVoteResult(command.getVoteId(), command.getResult());
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("投票成功");
        rs.setObj(vote);
        return rs;
    }

    @RequestMapping(value = "/find-votes-by-user", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findVoteByUser(@RequestParam String id) {
        Result rs = new Result();

        if (id == null) {
            Subject sub = SecurityUtils.getSubject();
            User loginUser = (User) sub.getPrincipal();
            id = loginUser.getId();
        }
        List<Vote> votes = null;
        try {
            votes = voteService.findVotesByUser(id);
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("查询用户所有投票成功");
        rs.setObj(votes);
        return rs;
    }

    @RequestMapping(value = "/find-votes-page", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findVoteByUser(@RequestBody FindVoteCommand command) {
        Result rs = new Result();

        List<Vote> votes = null;
        try {
            votes = voteService.findVotesWithPage(command);
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("查询第"+ command.getPage() +"页的投票项成功");
        rs.setObj(votes);
        return rs;
    }

    @RequestMapping(value = "/find-vote-by-id", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Result findVoteById(@RequestParam String id) {
        Result rs = new Result();

        Vote vote = null;
        try {
            vote = voteService.findVoteById(id);
        }catch (ServiceException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            return rs;
        }
        rs.setSuccess(true);
        rs.setMsg("查询投票成功");
        rs.setObj(vote);
        return rs;
    }

    /**
     * 下载
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> d(HttpServletRequest request ) throws Exception {

        String filename = "voteInfo.csv";
        
        FindVoteCommand findVoteCommand = new FindVoteCommand();
        findVoteCommand.setPage(1);
        findVoteCommand.setSize(999999);
        List<Vote> datas = voteService.findVotesWithPage(findVoteCommand);
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
