package com.shsxt.crm.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class JSONs {

    public static void toJsonString(HttpServletResponse response,Object object){
        //设置编码格式与返回json格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter wr = null;
        try {
            wr = response.getWriter();
            //将object对象转换为json,并写出
            wr.write(JSON.toJSONString(object));
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=wr){
                wr.close();
            }
        }
    }
}
