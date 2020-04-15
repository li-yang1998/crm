package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    public  List<String> queryUserHasRoleHasPermission(int userId){
        return  permissionMapper.queryUserHasRoleHasPermission(userId);
    }
}
