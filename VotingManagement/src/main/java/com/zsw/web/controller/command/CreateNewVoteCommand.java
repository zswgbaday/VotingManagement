package com.zsw.web.controller.command;

import com.zsw.common.enums.VoteStatus;
import com.zsw.pojo.vote.VoteContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class CreateNewVoteCommand {
    
    private VoteContent voteContent;
    private String status;
    private String stopTime;
    private MultipartFile uploadImage;

    public VoteContent getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(VoteContent voteContent) {
        this.voteContent = voteContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public MultipartFile getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(MultipartFile uploadImage) {
        this.uploadImage = uploadImage;
    }
}
