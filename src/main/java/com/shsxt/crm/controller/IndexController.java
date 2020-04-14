package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;
    /**
     * 登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        List<String> permissions=permissionService.queryUserHasRoleHasPermission(userId);
        request.getSession().setAttribute("permissions",permissions);
        //通过解密后的userId查询user
        User user = userService.selectByPrimaryKey(userId);
        //存储user对象
        request.setAttribute("user",user);
        return "main";
    }

    /**
     *  注册页面
     * @return
     */
    @RequestMapping("user/register")
    public String register(){
        return "register";
    }
}
