package com.zsw.web.controller;

import com.zsw.dao.mappers.TestMapper;
import com.zsw.pojo.Admin;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HelloController {


    @Resource
    private TestMapper testMapper;

    public void setTestMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }
    //构造器注入
//    public HelloController(TestMapper testMapper){
//        this.testMapper = testMapper;
//    }


    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello success !! \n\t --- zsw";
    }

    @RequestMapping("first-select")
    @ResponseBody
    public List<Admin> firstSelect(){
         List<Admin> list = testMapper.selectAll();
         return list;
    }

}
