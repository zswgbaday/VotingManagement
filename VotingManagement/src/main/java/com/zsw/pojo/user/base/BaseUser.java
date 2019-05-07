package com.zsw.pojo.user.base;

import com.zsw.common.util.Uuid;

import java.io.Serializable;

public abstract class BaseUser implements Serializable {

    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String createNewId() {
        this.id = Uuid.getUUID32();
        return this.id;
    }
}
