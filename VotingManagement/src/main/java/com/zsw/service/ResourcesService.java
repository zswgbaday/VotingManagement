package com.zsw.service;

import com.zsw.common.exception.ServiceException;
import com.zsw.dao.mappers.ResourcesMapper;
import com.zsw.dao.mappers.RoleResMapper;
import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.Role;
import com.zsw.pojo.role.RoleRes;
import com.zsw.web.controller.command.CreateNewResCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourcesService {

    private ResourcesMapper resourcesMapper;
    
    private RoleResMapper roleResMapper;

    @Autowired
    public ResourcesService(ResourcesMapper resourcesMapper, RoleResMapper roleResMapper) {
        this.resourcesMapper = resourcesMapper;
        this.roleResMapper = roleResMapper;
    }

    public List<Resources> getResourcesByRoles(Collection<Role> roles) {
        if (roles.size() == 0) {
            return new ArrayList<Resources>();
        }
        return resourcesMapper.findResourcesByRoleIds(roles.stream().map(role -> {
            return role.getId();
        }).collect(Collectors.toList()));
    }

    /**
     * 查询所有资源列表
     */
    public List<Resources> getAllRes() {
        return resourcesMapper.findAllResources().stream().collect(Collectors.toList());
    }
    
    //查询资源
    public List<Resources> getResByKeyWord(String keyWord) {
        return resourcesMapper.findResourcesByKeyWord(keyWord);
    }

    public boolean createNewRes(CreateNewResCommand command) {
        if (StringUtils.isBlank(command.getName())) {
            throw new ServiceException("投票id不能");
        }
        Resources res = new Resources();
        res.setName(command.getName());
        res.setStatus(command.getStatus());
        res.setUrl(command.getUrl());
        res.setIcon(command.getIcon());
        //懒得校验了，来不及了
        if (resourcesMapper.insertNewRes(res) > 0) {
            return true;
        }
        return false;
    }
    
    public boolean updateRes(CreateNewResCommand command) {
        if (command.getId() == null) {
            throw new ServiceException("修改资源id不能为空");
        }
        Resources res = new Resources();
        res.setId(command.getId());
        res.setName(command.getName());
        res.setStatus(command.getStatus());
        res.setUrl(command.getUrl());
        res.setIcon(command.getIcon());
        if (resourcesMapper.updateRes(res) < 1) {
            return true;
        }
        return false;
    }
    
    public boolean removeRes(Integer id) {
        if (id == null) {
            throw new ServiceException("删除资源id不能为空");
        }
        List<RoleRes> list = roleResMapper.findByResId(id);
        if (list != null && list.size() > 0) {
            throw new ServiceException("删除资源失败，该资源仍被某角色使用");
        }
        if (resourcesMapper.deleteRes(id) < 1) {
            return false;
        }
        return true;
    }

    public Resources getResById(Integer id) {
        if (id == null) {
            throw new ServiceException("查询资源id不能为空");
        }
        Resources res = resourcesMapper.findResById(id);
        return res;
    }
}
