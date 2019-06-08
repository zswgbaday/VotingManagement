package com.zsw.pojo.role;

import com.zsw.common.enums.ResourcesStatus;

import java.util.Objects;

public class Resources {
    
    private int id;
    private String name;
    private ResourcesStatus status;
    private String url;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourcesStatus getStatus() {
        return status;
    }

    public void setStatus(ResourcesStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resources resources = (Resources) o;
        return id == resources.id &&
                Objects.equals(name, resources.name) &&
                status == resources.status &&
                Objects.equals(url, resources.url) &&
                Objects.equals(icon, resources.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, url, icon);
    }
}
