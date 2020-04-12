package com.shsxt.crm.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.vo.SaleChance;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {


    public int updateDevResult(CusDevPlanQuery cusDevPlanQuery);
}