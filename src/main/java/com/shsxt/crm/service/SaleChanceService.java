package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer>{

    public Map<String,Object> querySaleChanceParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<>();
        //通过PageHelper进行分页
        PageHelper.startPage(saleChanceQuery.getPageNum(),saleChanceQuery.getPageSize());
        //查询客户记类
        PageInfo<SaleChance> pageInfo = new PageInfo<>(selectByParams(saleChanceQuery));
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }
}
