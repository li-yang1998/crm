package com.shsxt.crm.utils;

import com.shsxt.crm.exceptions.ParamsException;

/**
 *  断言工具类
 */
public class AssterUtil{
    public static void isTrue(Boolean flag,String msg){
        if(flag){
            throw new ParamsException(msg);
        }
    }
}
