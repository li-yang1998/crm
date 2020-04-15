package com.shsxt.crm.enums;

/**
 *  分配状态枚举类
 */
public enum Stateus {
    //未分配
    UNSTATE(0),
    //已分配
    STATEUS(1);

    private Integer type;

    Stateus(Integer type){
        this.type=type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
