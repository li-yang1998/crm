package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.annotaions.RequirePermission;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private UserService userService;

    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    public String index(){
        return "sale_chance";
    }

    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String,Object> querySaleChanceParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChanceParams(saleChanceQuery);
    }

    @RequestMapping("save")
    @ResponseBody
    @RequirePermission(code = "101002")
    public ResultInfo saveSaleChance(HttpServletRequest request,SaleChance saleChance){
        /**
         * 1.通过LoginUserUtils获取到登入的用户id
         * 2.在通过该id拿取到用户的真实姓名
         * 3.设置到SaleChance中
         */
        saleChance.setCreateMan(userService.selectByPrimaryKey(LoginUserUtil.releaseUserIdFromCookie(request)).getTrueName());
        saleChanceService.saveSaleChance(saleChance);
        return success("机会数据添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(code = "101004")
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("更新成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    @RequirePermission(code = "101003")
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("删除成功!");
    }
}
