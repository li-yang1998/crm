package com.shsxt.crm.enums;

/**
 *  分配状态枚举类
 */
public enum DevResult {
    //未发中
    UNDEV(0),
    //开发中
    DEVING(1),
    //开发成功
    DEV_SUCCESS(2),
    //开发失败
    DEV_FAILED(3);
    private Integer type;

    DevResult(Integer type){
        this.type=type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
