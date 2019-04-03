package com.zsw.dao.mappers;

import com.zsw.pojo.Admin;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface TestMapper {

    @Select("select * from admin")
    List<Admin> selectAll();
}
