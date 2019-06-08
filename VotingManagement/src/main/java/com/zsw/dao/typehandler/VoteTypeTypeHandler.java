package com.zsw.dao.typehandler;

import com.zsw.common.enums.VoteType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteTypeTypeHandler implements TypeHandler<VoteType> {
    @Override
    public void setParameter(PreparedStatement ps, int i, VoteType voteType, JdbcType jdbcType) throws SQLException {
        ps.setString(i, voteType.name());
    }

    @Override
    public VoteType getResult(ResultSet resultSet, String s) throws SQLException {
        return VoteType.valueOf( resultSet.getString(s));
    }

    @Override
    public VoteType getResult(ResultSet resultSet, int i) throws SQLException {
        return VoteType.valueOf( resultSet.getString(i));
    }

    @Override
    public VoteType getResult(CallableStatement cs, int i) throws SQLException {
        return VoteType.valueOf( cs.getString(i));
    }
}
