package com.shsxt.crm.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.dto.TestDto;
import com.shsxt.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    public List<TestDto> queryAllModules();

    Module  queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module  queryModuleByGradeAndUrl(@Param("grade") Integer grade, @Param("url") String url);

    Module  queryModuleByOptValue(String optValue);


    public List<Map<String, Object>>  queryAllModuleByGrade(Integer grade);

    public int countSubModuleByParentId(Integer mid);

    public List<ModuleDto> queryUserHasModuleDtos(@Param("userId")Integer userId, @Param("grader")Integer grader, @Param("pid")Integer pid);
}