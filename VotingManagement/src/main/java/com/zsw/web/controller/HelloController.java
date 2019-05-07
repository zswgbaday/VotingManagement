package com.zsw.web.controller;

import com.zsw.dao.mappers.HelloMapper;
import com.zsw.pojo.Hello;
import com.zsw.pojo.user.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

    private HelloMapper helloMapper;

    /**
     * 构造器注入
     */
    @Autowired
    public HelloController(HelloMapper helloMapper){
        this.helloMapper = helloMapper;
    }


    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello success !! \n\t --- zsw";
    }

    @RequestMapping("first-select")
    @ResponseBody
    public List<Hello> firstSelect(){
         List<Hello> list = helloMapper.selectAll();
         return list;
    }

    @RequestMapping("anno-test")
    @ResponseBody
    public String annoTest(){
        return "";
    }

}
