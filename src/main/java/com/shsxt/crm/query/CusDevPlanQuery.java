package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

public class CusDevPlanQuery extends BaseQuery {
    // 营销机会id
    private Integer sid;
    private Integer devResult;

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
