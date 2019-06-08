package com.zsw.dao.typehandler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsw.pojo.vote.VoteOption;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;

/**
 * 专门用来存放VoteOption
 */
public class VoteOptionsTypeHandler implements TypeHandler<List<VoteOption>> {

    private Gson gson = new Gson();
    private Type typeToken = new TypeToken<List<VoteOption>>(){}.getType();
    
    @Override
    public void setParameter(PreparedStatement ps, int i, List<VoteOption> voteOptions, JdbcType jdbcType) throws SQLException {
        String jsonOptions = gson.toJson(voteOptions);
        StringReader reader = new StringReader(jsonOptions);
        ps.setCharacterStream(i, reader, jsonOptions.length());
    }

    @Override
    public List<VoteOption> getResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = "";
        Clob clob = resultSet.getClob(columnName);
        if (clob != null ) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return gson.fromJson(value, typeToken);
    }

    @Override
    public List<VoteOption> getResult(ResultSet resultSet, int i) throws SQLException {
        String value = "";
        Clob clob = resultSet.getClob(i);
        if (clob != null ) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return gson.fromJson(resultSet.getString(i), typeToken);
    }

    @Override
    public List<VoteOption> getResult(CallableStatement cs, int i) throws SQLException {
        String value = "";
        Clob clob = cs.getClob(i);
        if (clob != null ) {
            int size = (int) clob.length();
            value = clob.getSubString(1L, size);
        }
        return gson.fromJson(cs.getString(i), typeToken);
    }
}
