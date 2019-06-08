package com.zsw.dao.typehandler;

import com.zsw.common.enums.RoleType;
import com.zsw.pojo.role.Role;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SetRoleForUserTypeHandler implements TypeHandler<Collection<Role>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Collection<Role> roles, JdbcType jdbcType) throws SQLException {
        if (! (roles instanceof Set)) {
            ps.setString(i, RoleType.ORDINARY.name());
            return;
        }
        if (roles == null || roles.size() == 0) {
            //默认设置用户为普通用户
            ps.setString(i, RoleType.ORDINARY.name());
        } else {
            int maxRoleId = 2147483647;
            Object[] objs = roles.toArray();
            for (int index = 0; index < objs.length; ++index) {
                int id = ((Role)objs[index]).getId();
                //role的id越小,权限越高
                maxRoleId = id < maxRoleId ? id : maxRoleId;
            }
            RoleType type = RoleType.getTypeByValue(maxRoleId);
            ps.setString(i, type.name());
        }
    }

    @Override
    public List<Role> getResult(ResultSet resultSet, String s) throws SQLException {
        return null;
    }

    @Override
    public List<Role> getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public List<Role> getResult(CallableStatement cs, int i) throws SQLException {
        return null;
    }
}
