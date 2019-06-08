package com.zsw.web.controller.command;

import com.zsw.common.enums.VoteStatus;

import java.util.Date;

public class UpdateVoteCommand {
    
    private String id;
    private VoteStatus status;
    private Date stopTime;
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
