package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.service.CusDevPlanService;
import com.shsxt.crm.vo.CusDevPlan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("index")
    public String index(){
        return "cus_dev_plan";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlan(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryByParamsForDataGrid(cusDevPlanQuery);
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("保存成功！");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("更新成功！");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除成功！");
    }

    @RequestMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateDevResult(CusDevPlanQuery cusDevPlanQuery){
        cusDevPlanService.updateDevResult(cusDevPlanQuery);
        return success("客户开发状态更新成功!");
    }
}
