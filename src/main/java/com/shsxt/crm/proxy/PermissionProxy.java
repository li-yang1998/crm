package com.shsxt.crm.proxy;

import com.shsxt.crm.annotaions.RequirePermission;
import com.shsxt.crm.exceptions.AuthFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    @Around(value = "@annotation(com.shsxt.crm.annotaions.RequirePermission)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //获取所有的权限码值
        List<String> permissions =(List<String>) session.getAttribute("permissions");
        //判断是否非空
        if(null==permissions || permissions.size()==0){
            throw new AuthFailedException();
        }
        Object result = null;
        MethodSignature methodSignature =(MethodSignature)point.getSignature();
        RequirePermission requirePermission = methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);

        if(!(permissions.contains(requirePermission.code()))){
            throw new AuthFailedException();
        }
        result=point.proceed();
        return  result;
    }
}
