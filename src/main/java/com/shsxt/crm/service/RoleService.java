package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dao.RoleMapper;
import com.shsxt.crm.query.RoleQuery;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;
    /**
     * 查询所有roleName
     * @return
     */
    public  List<Map<String, Object>> queryAllRole(){
        return roleMapper.queryAllRoles();
    }

    /**
     * 用户多条件查询
     * @param roleQuery
     * @return
     */
    public Map<String,Object> selectParams(RoleQuery roleQuery){
        return queryByParamsForDataGrid(roleQuery);
    }

    /**
     *  用户角色添加
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public  void saveRole(Role role){
        AssterUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleName(role.getRoleName());
        AssterUtil.isTrue(null !=temp,"该角色已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssterUtil.isTrue(insertSelective(role)<1,"角色记录添加失败!");
    }

    /**
     *  用户角色修改
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        /**
         *  参数校验:
         *      roleName  非空
         *      role_remark 非空
         *   默认值设置:
         *       updateDate 当前系统时间
         *   修改的角色名是否已存在
         *   执行修改,判断结果
         */
        AssterUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空!");
        AssterUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()),"备注不能为空!");
        Role temp = roleMapper.selectRoleByName(role.getRoleName());
        AssterUtil.isTrue(null!=temp,"该角色名已存在,请换一个!");

        role.setUpdateDate(new Date());
        AssterUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色修改失败!");
    }

    public void deleteRole(Integer id){
        /**
         * 参数校验:
         *      ids 非空
         *  查询ids中的值是否在数据库中存在
         *  执行删除,判断结果
         */
        AssterUtil.isTrue(null==id,"网络异常,请重试!");

            Role role = selectByPrimaryKey(id);
            AssterUtil.isTrue(null==role,"暂无该记录,请先添加!");
            role.setIsValid(0);
            AssterUtil.isTrue(deleteByPrimaryKey(id)<1,"删除失败,请查看该记录是否存在!");
        }

    public  void addGrant(Integer[] mids, Integer roleId){
        /**
         *  核心表:t_permission  t_role校验角色存在
         *   批量添加权限记录到t_permission
         */
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AssterUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)<count,"权限分配失败!");
        }
        if(null!=mids && mids.length>0){
            List<Permission> permissions = new ArrayList<>();
            for (Integer mid:mids){
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //从module表中获取到权限码值
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            permissionMapper.insertBatch(permissions);
        }

    }
}
