package com.zsw.web.controller.command;

public class FindUsersByRuleCommand {
    private Integer page;
    private Integer size;
    private String name;
    private String email;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }
    
    /**mysql查询,得到偏移量 */
    public Integer getOffset(){
        return (page-1) * size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
