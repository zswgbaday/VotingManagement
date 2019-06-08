package com.zsw.pojo.vote;

import com.zsw.common.enums.VoteStatus;
import com.zsw.pojo.BasePojo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Vote -- 投票项 和 发起投票的人的, 什么时候发起,什么时候结束
 * VoteContent -- 一个投票的内容,结构,等等
 * VoteOption -- 投票选项
 */
public class Vote extends BasePojo {
    private String id;
    private VoteStatus status;
    private Date stopTime;
    private VoteContent voteContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VoteStatus getStatus() {
        return status;
    }

    public void setStatus(VoteStatus status) {
        this.status = status;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public VoteContent getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(VoteContent voteContent) {
        this.voteContent = voteContent;
    }

    public Vote(Date createTime, String createById, String createByName, String id, VoteStatus status, Date stopTime, VoteContent voteContent) {
        super(createTime, createById, createByName);
        this.id = id;
        this.status = status;
        this.stopTime = stopTime;
        this.voteContent = voteContent;
    }

    public Vote() {
    }
}
