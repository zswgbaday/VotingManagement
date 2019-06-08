package com.zsw.web.controller.command;

import java.util.List;

public class UpdateVoteResultComand {
    
    private String voteId;
    private List<Integer> result;

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public List<Integer> getResult() {
        return result;
    }

    public void setResult(List<Integer> result) {
        this.result = result;
    }
}
