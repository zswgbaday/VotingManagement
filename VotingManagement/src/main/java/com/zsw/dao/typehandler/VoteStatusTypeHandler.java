package com.zsw.dao.typehandler;

import com.zsw.common.enums.VoteStatus;
import com.zsw.pojo.vote.Vote;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteStatusTypeHandler implements TypeHandler<VoteStatus> {
    @Override
    public void setParameter(PreparedStatement ps, int i, VoteStatus voteStatus, JdbcType jdbcType) throws SQLException {
        ps.setString(i, voteStatus.name());
    }

    @Override
    public VoteStatus getResult(ResultSet resultSet, String s) throws SQLException {
        return  VoteStatus.valueOf(resultSet.getString(s));
    }

    @Override
    public VoteStatus getResult(ResultSet resultSet, int i) throws SQLException {
        return VoteStatus.valueOf(resultSet.getString(i));
    }

    @Override
    public VoteStatus getResult(CallableStatement cs, int i) throws SQLException {
        return VoteStatus.valueOf(cs.getString(i));
    }
}
