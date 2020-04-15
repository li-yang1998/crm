package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.CusDevPlanMapper;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        /**
         *  参数校验:
         *      营销机会id       非空
         *      计划项的内容     非空
         *      计划时间        非空
         *  参数默认值设置:
         *      is_valid createDate updateDate
         *  执行添加操作,判断结果
         *
         */
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssterUtil.isTrue(insertSelective(cusDevPlan)<1,"保存失败,请稍后重试!");
    }

    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        AssterUtil.isTrue(null==saleChanceId || null==saleChanceMapper.selectByPrimaryKey(saleChanceId),"请设置营销计划id");
        AssterUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划内容");
        AssterUtil.isTrue(null==planDate,"请指定计划项日期");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        /**
         *  参数校验:
         *      营销机会id       非空
         *      计划项的内容     非空
         *      计划时间        非空
         *  参数默认值设置:
         *      is_valid createDate updateDate
         *  执行添加操作,判断结果
         */
        //先判断该营销机会是否在数据库中存在
        AssterUtil.isTrue(null==cusDevPlan.getId()||null==selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不存在!");
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        cusDevPlan.setUpdateDate(new Date());
        AssterUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"更新失败,请稍后重试!");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        /**
         *  参数校验：
         *      id值 非空
         *   执行删除,判断结果
         */
        CusDevPlan cusDevPlan = selectByPrimaryKey(id);
        AssterUtil.isTrue(null==cusDevPlan||null==id,"待删除记录不存在!");
        cusDevPlan.setIsValid(0);
        AssterUtil.isTrue(deleteByPrimaryKey(id)<1,"删除失败,请稍后重试!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDevResult(CusDevPlanQuery cusDevPlanQuery){
        /**
         *  参数校验:
         *      sid 非空
         *      devResult 非空
         *   执行更新,判断结果
         */
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(cusDevPlanQuery.getSid());
        AssterUtil.isTrue(null==saleChance.getId()||null==cusDevPlanQuery.getSid(),"暂无该客户,请先添加!");
        AssterUtil.isTrue(null==cusDevPlanQuery.getDevResult(),"客户开放状态无,请检查!");
        AssterUtil.isTrue(saleChanceMapper.updateDevResult(cusDevPlanQuery)<1,"客户开放状态修改失败,请稍后重试!");
    }


}
