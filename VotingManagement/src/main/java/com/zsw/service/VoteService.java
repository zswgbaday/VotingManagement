package com.zsw.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsw.common.enums.VoteStatus;
import com.zsw.common.exception.ServiceException;
import com.zsw.common.util.Uuid;
import com.zsw.dao.mappers.VoteMapper;
import com.zsw.pojo.user.User;
import com.zsw.pojo.vote.Vote;
import com.zsw.pojo.vote.VoteContent;
import com.zsw.pojo.vote.VoteOption;
import com.zsw.pojo.vote.VoteResult;
import com.zsw.web.controller.command.CreateNewVoteCommand;
import com.zsw.web.controller.command.FindVoteCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class VoteService {
    
    private VoteMapper voteMapper;
    
    @Autowired
    public VoteService(VoteMapper voteMapper) {
        this.voteMapper = voteMapper;
    }
    
    public Vote insertNewVote(CreateNewVoteCommand command, User user) {
        if (command == null || user == null) {
            throw new ServiceException("创建投票失败,投票为{},用户为{},请重试", command, user);
        }
        //处理Vote
        Vote newVote = new Vote();
        newVote.setId(Uuid.getUUID32());
        try {
            VoteStatus voteStatus =  VoteStatus.valueOf(command.getStatus());
            newVote.setStatus(voteStatus);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date stopTime = sdf.parse(command.getStopTime());
            newVote.setStatus(voteStatus);
            newVote.setStopTime(stopTime);
        }catch (IllegalArgumentException e1) {
            throw new ServiceException("获取投票状态失败,请重试");
        }catch (ParseException e2) {
            throw new ServiceException("获取投票截止时间{}错误,请重试", command.getStopTime());
        }
        //处理VoteContent
        checkVoteContent(newVote, command);
        newVote.setCreateById(user.getId());
        newVote.setCreateByName(user.getName());
        //持久化
        if (voteMapper.insertNewVote(newVote) > 0 ) {
            return newVote;
        }else {
            throw new ServiceException("创建投票失败,请重试");
        }
    }
    
    public Vote updateVoteStatus(String id, VoteStatus status, Date stopTime, String title) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("投票id不能为空");
        }
        if (voteMapper.updateVoteStatus(id, status, stopTime, title) > 0) {
            return voteMapper.findVoteById(id);
        }else {
            throw new ServiceException("更新投票失败");
        }
    }

    public Vote updateVoteResult(String id, List<Integer> _optionsRs) {
        Vote vote = new Vote();
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("投票id不能为空");
        }
        Vote thisVote = voteMapper.findVoteById(id);
        //验证投票状态
        if (thisVote.getStatus() == VoteStatus.DISABLE) {
            throw new ServiceException("投票已经关闭，无法投票");
        }
        if (thisVote.getStopTime().getTime() <= System.currentTimeMillis()) {
            if (thisVote.getStatus() != VoteStatus.DISABLE) {
                voteMapper.updateVoteStatus(thisVote.getId(), VoteStatus.DISABLE, null, null);
            }
            throw new ServiceException("投票已经到期，若要投票请联系发起者修改投票截至时间");
        }
        //唉，写不动了，瞎写吧
        String options = voteMapper.findVoteResultById(id);
        if ( StringUtils.isBlank(options)) {
            throw new ServiceException("没有id为{}的投票, 请刷新后重新投票", id);
        }
        options = addVoteResult(options, _optionsRs);
        
        if (voteMapper.updateVoteResult(id, options) > 0) {
            return voteMapper.findVoteById(id);
        }else {
            throw new ServiceException("投票失败");
        }
    }

    public List<Vote> findVotesByUser(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("用户id不能为空");
        }
        return  voteMapper.findVoteByUserId(id);
    }
    
    public List<Vote> findVotesWithPage(FindVoteCommand command) {
        if (command.getPage() == null || command.getSize() == null) {
            throw new ServiceException("查询错误,请检查页码");
        }
        return  voteMapper.findVoteWithPage(command);
    }

    public Vote findVoteById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("投票id不能为空");
        }
        return  voteMapper.findVoteById(id);
    }
    
    
    /**********************************/

    /**
     * 校验VoteContent, 处理content
     */
    private Vote checkVoteContent(Vote vote, CreateNewVoteCommand command) {
        VoteContent commandContent = command.getVoteContent();
        //处理投票项
        if (StringUtils.isBlank(commandContent.getTitle())) {
            throw new ServiceException("投票标题不能为空");
        }
        if (commandContent.getOptions() == null || commandContent.getOptions().size() == 0) {
            throw new ServiceException("投票选项不能为空");
        }
        VoteContent content = new VoteContent();
        //vote和voteContent是在一张表里的,所这个id根本没必要
        content.setId(vote.getId());
        content.setTitle(commandContent.getTitle());
        content.setDescribe(commandContent.getDescribe());
        content.setImage(commandContent.getImage());
        content.setType(commandContent.getType());
        //处理 投票选选项
//        VoteResult vs = new VoteResult(); //VoteResult弃之不用
        List<VoteOption> optionList = commandContent.getOptions();
        content.setOptions(optionList);
        for (int i = 0; i < optionList.size(); ++ i) {
            VoteOption option = optionList.get(i);
            if (StringUtils.isBlank(option.getOptionDescribe())) {
                throw new ServiceException("创建投票失败, 投票项描述不能为空");
            }
            //voteOption的下表也是从0开始的
            option.setId(i);
            //初始化投票数
            option.setCount(0);
        }
        vote.setVoteContent(content);
        return vote;
    }

    /**
     * 更新投票结果
     * @param dbOption 数据库中的投票 (结构)
     * @param voteOption    新提交的投票结果(count)
     */
    private String addVoteResult(String dbOption, List<Integer> voteOption) {
        Gson gson = new Gson();
        Type typeToken = new TypeToken<List<VoteOption>>(){}.getType();
        List<VoteOption> optionsStruct = gson.fromJson(dbOption, typeToken);
        try {
            for (Integer i : voteOption) {
                VoteOption option = optionsStruct.get(i);
                option.setCount(option.getCount() + 1);
            }
        }catch (IndexOutOfBoundsException e) {
            if (optionsStruct.size() != voteOption.size()) {
                throw new ServiceException("投票只有{}个选项, 您选择了{}个选项,投票失败", optionsStruct.size(), voteOption.size());
            } else {
                throw new ServiceException("投票只有0-{}个选项, 您的选择超出范围,投票失败", optionsStruct.size());
            }
        }
        return gson.toJson(optionsStruct);
    }

    public void removeVoteById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("删除投票id不能为空");
        }
        Integer rs = voteMapper.deleteVoteById(id);
        if (rs < 0) {
            throw new ServiceException("删除id为{}的投票失败", id);
        }
    }

    public boolean upLoadPhoto(String id, byte[] bytes) {
        if (voteMapper.setPhotoById(id, bytes) > 0) {
            return true;
        }
        return false;
    }

    public byte[] getPhotoById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("投票id不能为空");
        }
        Vote vote = voteMapper.getPhotoById(id);
        return vote.getVoteContent().getImage();
    }
}
