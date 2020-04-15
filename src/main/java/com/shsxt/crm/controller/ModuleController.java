package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.dto.TestDto;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.ModuleQuery;
import com.shsxt.crm.service.ModuleService;
import com.shsxt.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @RequestMapping("/index/{grade}")
    public String index(@PathVariable Integer grade, Integer mid, Model model){
        model.addAttribute("mid",mid);
        if(grade==1){
            return "module_1";
        }else  if(grade==2){
            return "module_2";
        }else if(grade==3){
            return "module_3";
        }else{
            return "";
        }
    }



    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TestDto> queryAllModule(Integer roleId){
        return moduleService.queryAllModules02(roleId);
    }



    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveModule(Module module){
        moduleService.saveModule(module);
        return success("添加成功!");
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModule(ModuleQuery moduleQuery){
        return moduleService.queryModule(moduleQuery);
    }

    @RequestMapping("queryAllModuleByGrade")
    @ResponseBody
    public List<Map<String,Object>> queryAllModuleByGrade(Integer grade){
        return moduleService.queryAllModuleByGrade(grade);
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("修改成功!");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除成功!");
    }

}
