package com.zsw.web.controller.command;

import com.zsw.common.enums.ResourcesStatus;

public class CreateNewResCommand {
    
    private Integer id;
    private String name;
    private String url;
    private ResourcesStatus status;
    private String icon;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResourcesStatus getStatus() {
        return status;
    }

    public void setStatus(ResourcesStatus status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
