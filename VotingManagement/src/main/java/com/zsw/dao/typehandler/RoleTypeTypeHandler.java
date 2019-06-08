package com.zsw.dao.typehandler;

import com.zsw.common.enums.RoleType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleTypeTypeHandler implements TypeHandler<RoleType> {
    @Override
    public void setParameter(PreparedStatement ps, int i, RoleType roleType, JdbcType jdbcType) throws SQLException {
        ps.setString(i, roleType.name());
    }

    @Override
    public RoleType getResult(ResultSet resultSet, String s) throws SQLException {
        return RoleType.valueOf(resultSet.getString(s));
    }

    @Override
    public RoleType getResult(ResultSet resultSet, int i) throws SQLException {
        return RoleType.valueOf(resultSet.getString(i));
    }

    @Override
    public RoleType getResult(CallableStatement cs, int i) throws SQLException {
        return RoleType.valueOf(cs.getString(i));
    }
}
