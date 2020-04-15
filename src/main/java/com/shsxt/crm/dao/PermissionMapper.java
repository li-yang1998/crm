package com.shsxt.crm.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.vo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    public int countPermissionByRoleId(Integer roleId);

    public int deletePermissionByRoleId(Integer roleId);

    public List<Integer> queryRoleHasAllNoduleIds(Integer roleId);

    public List<String> queryUserHasRoleHasPermission(int userId);

    public int deeletePermissionByModuleId(Integer mid);

    public int selectPermissionByModule(Integer mid);


}