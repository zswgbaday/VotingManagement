package com.zsw.dao.mappers;

import com.zsw.pojo.Hello;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface HelloMapper {

    @Select("select * from v_test")
    List<Hello> selectAll();
}
