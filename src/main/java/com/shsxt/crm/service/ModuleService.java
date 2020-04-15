package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.dto.TestDto;
import com.shsxt.crm.query.ModuleQuery;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public List<TestDto> queryAllModule(){
        return moduleMapper.queryAllModules();
    }

    public List<TestDto> queryAllModules02(Integer roleId) {
        List<TestDto> treeDtos=moduleMapper.queryAllModules();

        // 根据角色id 查询角色拥有的菜单id  List<Integer>
        List<Integer> roleHasMids=permissionMapper.queryRoleHasAllNoduleIds(roleId);

        if(null !=roleHasMids && roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if(roleHasMids.contains(treeDto.getId())){
                    //  说明当前角色 分配了该菜单
                    treeDto.setChecked(true);
                }
            });
        }
        return  treeDtos;
    }

    public Map<String,Object> queryModule(ModuleQuery moduleQuery){
        return queryByParamsForDataGrid(moduleQuery);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module){
        /**
         *  1.参数校验：
         *      module_name  非空,同一层级下模块名唯一
         *      url:
         *          二级菜单 非空 不可重复
         *       上级菜单 -parent_id
         *          一级菜单 null
         *          二级|三级菜单 parent_id 非空,必须存在
         *          层级-grade:
         *              非空 0|1|2
         *          权限码-opt_value:
         *              非空,唯一
         *   2.参数默认值:
         *      is_valid createData updateDate
         *   3.执行添加,判断结果
         */
        AssterUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入菜单名");
        Integer grade = module.getGrade();
        AssterUtil.isTrue((null==grade || !(grade==1||grade==2||grade==0)),"菜单不合法");
        AssterUtil.isTrue(null !=moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName()),"该层级下菜单重复!");
        if(grade==1){
            AssterUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请指定二级菜单url值");
            AssterUtil.isTrue(null !=moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl()),"二级菜单url不可重复!");
        }
        if(grade!=0){
            Integer parentId = module.getParentId();
            AssterUtil.isTrue(null==parentId||null==selectByPrimaryKey(parentId),"请指定上级菜单!");
        }
        AssterUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码!");
        AssterUtil.isTrue(!(null==moduleMapper.queryModuleByOptValue(module.getOptValue())),"权限码重复!");
        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssterUtil.isTrue(insertSelective(module)<1,"菜单添加失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        /**
         * 更新:
         *  参数校验：
         *      id          非空
         *   模块间的校验:
         *      模块名:module_name  非空,同一层级下模块名唯一
         *      url: 二级菜单,非空,不可重复
         *      上级菜单: -parent_id
         *          一级菜单 null
         *          二级菜单/三级菜单   parent_id非空 必须存在
         *          层级-grade
         *              非空  0/1/2
         *              权限吗 optValue
         *                  非空 不可重复
         *   2.参数默认
         *      is_valid updateDate
         *   3.执行更新,判断结果
         *
         */
        Integer id = module.getId();
        AssterUtil.isTrue(null==id || !(null==selectByPrimaryKey(id)),"id值为空或暂无该记录！");
        AssterUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请指定菜单名称!");
        Integer grade = module.getGrade();
        AssterUtil.isTrue(null==grade||!(grade==0||grade==1||grade==2),"菜单层级不合法!");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName());
        if(null!=temp){
            AssterUtil.isTrue(temp.getId().equals(module.getId()),"该层级下菜单已存在!");
        }
        if(grade==1){
            AssterUtil.isTrue(null==module.getUrl(),"请指定二级菜单url值");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl());
            if(null!=temp){
                AssterUtil.isTrue(temp.getId().equals(module.getId()),"该层级下菜单已存在!");
            }
        }
        if(grade!=0){
            Integer parentId = module.getParentId();
            AssterUtil.isTrue(null==parentId||null==selectByPrimaryKey(parentId),"请指定上级菜单!");
        }
        /**
         * 权限吗 optValue 非空 不可重复
         */
        AssterUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码!");
        temp  = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if(null!=temp){
            AssterUtil.isTrue(temp.getOptValue().equals(module.getOptValue()),"权限码已存在!");
        }
        AssterUtil.isTrue(updateByPrimaryKeySelective(module)<1,"更新失败,请重试！");
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer mid){
        /**
         * 参数校验：
         *      id 非空,数据库中存在该值
         *   执行删除,判断结果
         */
        Module temp = selectByPrimaryKey(mid);
        AssterUtil.isTrue(null==mid || null==temp,"待删除的记录不存在!");
        /**
         * 如果存在子级菜单,不允许删除
         *
         */
        int count = moduleMapper.countSubModuleByParentId(mid);
        AssterUtil.isTrue(count>0,"存在子菜单,不支持删除!");
        AssterUtil.isTrue(deleteByPrimaryKey(mid)<1,"删除失败,请重试!");

        //权限表是否有值
        count = permissionMapper.selectPermissionByModule(mid);
        if(count>0){
            //有值，删除
            AssterUtil.isTrue(permissionMapper.deeletePermissionByModuleId(mid)<count,"删除失败!");
        }
        //将失效值,更新下
        temp.setIsValid((byte)0);
        AssterUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"菜单删除失败!");
    }

    public List<Map<String, Object>> queryAllModuleByGrade(Integer grade){
       return moduleMapper.queryAllModuleByGrade(grade);
    }


    public List<ModuleDto> queryUserHasRoleHasModuleDtos(Integer userId){
        /**
         *  1.查询用户角色分配的一级菜单
         *  2.根据一级菜单查询用户角色分配的二级菜单
         */
        List<ModuleDto> moduleDtos = moduleMapper.queryUserHasModuleDtos(userId, 0, null);
        if(null!=moduleDtos && moduleDtos.size()>0){
            moduleDtos.forEach(moduleDto -> {
                moduleDto.setSubModules(moduleMapper.queryUserHasModuleDtos(userId,1,moduleDto.getId()));
            });
        }
        return moduleDtos;
    }

}
