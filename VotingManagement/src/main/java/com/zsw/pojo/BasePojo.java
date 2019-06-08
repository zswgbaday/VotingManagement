package com.zsw.pojo;

import java.io.Serializable;
import java.util.Date;

public class BasePojo implements Serializable {
    
    private Date createTime;
    private String createById;
    private String createByName;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createBy) {
        this.createById = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public BasePojo(Date createTime, String createById, String createByName) {
        this.createTime = createTime;
        this.createById = createById;
        this.createByName = createByName;
    }

    public BasePojo() {
    }
}
