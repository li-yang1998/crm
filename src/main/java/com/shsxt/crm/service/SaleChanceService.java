package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.Stateus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    public Map<String,Object> querySaleChanceParams(SaleChanceQuery saleChanceQuery){
        return queryByParamsForDataGrid(saleChanceQuery);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        /**
         *  1.参数校验:
         *      customerName: 非空
         *      linkMan:非空
         *      linkPhone:非空
         *
         *  2.设置相关参数的默认值:
         *      state:默认未分配  如果选择分配人 state为已分配
         *      assignTime:如果选择分配人  时间为当前系统时间
         *      devResult: 默认未开发 如果选择分配人 devResult为开发中
         *      isValid: 默认有效
         *      createDate updateDate 系统当前时间
         *  3.执行添加 获取结果
         */
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setState(Stateus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getType());
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(Stateus.STATEUS.getType());
            saleChance.setDevResult(DevResult.DEVING.getType());
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssterUtil.isTrue(insertSelective(saleChance)<1,"机会数据添加失败!");
    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssterUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AssterUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssterUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系电话");
        AssterUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号格式不合法");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        /**
         *  1.参数校验:
         *       customerName: 非空
         *       linkMan:非空
         *       linkPhone:非空
         * 2. 设置相关参数值
         *      updateDate:系统当前时间
         *         原始记录 未分配 修改后改为已分配(由分配人决定)
         *            state 0->1
         *            assginTime 系统当前时间
         *            devResult 0-->1
         *         原始记录  已分配  修改后 为未分配
         *            state  1-->0
         *            assignTime  待定  null
         *            devResult 1-->0
         * 3.执行更新 判断结果
         */
        AssterUtil.isTrue(null==saleChance.getId(),"待更新用户不存在!");
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssterUtil.isTrue(null==temp,"待更新用户不存在!");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(Stateus.STATEUS.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(Stateus.UNSTATE.getType());
        }else if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(Stateus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(Stateus.UNSTATE.getType());
        }
        //无论如何都更新时间
        saleChance.setUpdateDate(new Date());
        //执行更新
        AssterUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"更新失败,请重试!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        AssterUtil.isTrue(ids.length==0||null==ids,"请选择需要删除的记录!");
        AssterUtil.isTrue(deleteBatch(ids)<ids.length,"更新失败,请稍后重试!");
    }

}
