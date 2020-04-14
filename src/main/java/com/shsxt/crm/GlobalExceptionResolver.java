package com.shsxt.crm;


import com.shsxt.crm.exceptions.AuthFailedException;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.utils.JSONs;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv=new ModelAndView();
        /**
         * 首先判断异常类型
         *   1.如果异常类型为未登录异常  执行视图转发
         *   2.异常登入,返回异常信息
         */
        if(ex instanceof NoLoginException){
            NoLoginException noLoginException = (NoLoginException) ex;
            mv.setViewName("no_login");
            mv.addObject("msg",noLoginException.getMsg());
            mv.addObject("ctx",request.getContextPath());
            return mv;
        }

        /**方法返回值类型判断:
         *    如果方法级别存在@ResponseBody 方法响应内容为json  否则视图
         *    handler 参数类型为HandlerMethod
         * 返回值
         *    视图:默认错误页面
         *
         *
         *
         *    json:错误的json信息
         */
        mv.setViewName("error");
        mv.addObject("code","500");
        mv.addObject("msg","系统异常,请稍后重试!");
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);

            if (null == responseBody || "".equals(responseBody)) {
                //返回视图
                if (ex instanceof ParamsException) {
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("msg", pe.getMsg());
                    mv.addObject("code", pe.getCode());
                }
                if(ex instanceof AuthFailedException){
                    AuthFailedException pe = (AuthFailedException) ex;
                    mv.addObject("msg",pe.getMsg());
                    mv.addObject("code",pe.getCode());
                }
                return mv;
            } else {
                //返回json
                ResultInfo resultInfo = new ResultInfo();
                /**
                 *  分2种异常
                 *        Exception处理：
                 *
                 *        ParamsException处理：
                 */
                // Exception处理：
                resultInfo.setCode(500);
                resultInfo.setMsg("网络异常,请稍后重试!");
                //ParamsException处理：
                if (ex instanceof ParamsException) {
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setMsg(pe.getMsg());
                    resultInfo.setCode(pe.getCode());
                }
                //将resultInfo转换为json并响应出去
                JSONs.toJsonString(response, resultInfo);
                return null;
            }
        }else{
            return mv;
        }
    }
}
