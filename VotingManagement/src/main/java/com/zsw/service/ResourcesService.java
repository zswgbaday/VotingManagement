package com.zsw.service;

import com.zsw.dao.mappers.ResourcesMapper;
import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourcesService {

    private ResourcesMapper resourcesMapper;

    @Autowired
    public ResourcesService(ResourcesMapper resourcesMapper) {
        this.resourcesMapper = resourcesMapper;
    }

    public List<Resources> getResourcesByRoles(Collection<Role> roles) {
        return resourcesMapper.findResourcesByRoleIds(roles.stream().map(role -> {
            return role.getId();
        }).collect(Collectors.toList()));
    }
}
