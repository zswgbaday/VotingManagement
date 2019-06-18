package com.zsw.pojo.role;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class Role {

    private int id;
    private String name;
    private String icon;

    /**
     * 角色对应的资源列表
     */
    private Set<Resources> resources;
    

    public Role(int id, String name, String icon, Set<Resources> resources) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.resources = resources;
    }

    public Role() {
    }

    

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(List<Resources> resources) {
        this.resources = resources.stream().collect(Collectors.toSet());
    }



    @Override
    public int hashCode() {
        return  Objects.hash(name, id, icon);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return  true;
        }
        if (! (obj instanceof Role)) {
            return false;
        }
        Role role = (Role)obj;
        return id == role.id &&
                Objects.equals(name, role.name) &&
                Objects.equals(icon, role.icon);
                
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
