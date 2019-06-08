package com.zsw.dao.typehandler;

import com.zsw.common.enums.ResourcesStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourcesStatuTypeHandler implements TypeHandler<ResourcesStatus> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ResourcesStatus resourcesStatus, JdbcType jdbcType) throws SQLException {
        ps.setString(i, resourcesStatus.name());
    }

    @Override
    public ResourcesStatus getResult(ResultSet resultSet, String s) throws SQLException {
        return ResourcesStatus.valueOf(resultSet.getString(s));
    }

    @Override
    public ResourcesStatus getResult(ResultSet resultSet, int i) throws SQLException {
        return ResourcesStatus.valueOf(resultSet.getString(i));
    }

    @Override
    public ResourcesStatus getResult(CallableStatement cs, int i) throws SQLException {
        return ResourcesStatus.valueOf(cs.getString(i));
    }
}
