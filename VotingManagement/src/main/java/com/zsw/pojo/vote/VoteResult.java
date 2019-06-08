package com.zsw.pojo.vote;

import java.util.Map;

/**
 * @author zswgbaday
 */
public class VoteResult {
    /** 投票结果 */
    private Map<Integer, Integer> voteCount;

    public Map<Integer, Integer> getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Map<Integer, Integer> voteCount) {
        this.voteCount = voteCount;
    }
}
