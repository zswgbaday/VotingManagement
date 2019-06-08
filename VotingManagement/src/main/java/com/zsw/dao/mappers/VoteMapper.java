package com.zsw.dao.mappers;

import com.zsw.common.enums.VoteStatus;
import com.zsw.pojo.vote.Vote;
import com.zsw.web.controller.command.FindVoteCommand;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VoteMapper {

    
    Integer insertNewVote(Vote vote);

    Integer updateVoteStatus(@Param("id") String id, @Param("status") VoteStatus status, @Param("stopTime") Date stopTime
        ,@Param("title") String title);
    
    List<Vote> findVoteByUserId(String userId);

    List<Vote> findVoteWithPage(FindVoteCommand command);
    
    /** 用于打开投票页 */
    Vote findVoteById(String voteId);
    
    /** 用于更新投票结果 */
    Integer updateVoteResult(@Param("voteId") String voteId, @Param("options") String options);
    String findVoteResultById(String voteId);

    Integer deleteVoteById(String id);

    Integer setPhotoById(@Param("id") String id, @Param("image") byte[] bytes);

    Vote getPhotoById(String id);
}
