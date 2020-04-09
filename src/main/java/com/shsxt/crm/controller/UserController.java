package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("user/queryUserById")
    @ResponseBody
    public User queryUserById(Integer userId){
        return userService.selectByPrimaryKey(userId);
    }

    /**
     *
     * @param userName
     * @param userPwd
     */
    @RequestMapping("user/login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        UserModel userModel = userService.queryUserByNameAndPwd(userName, userPwd);
        return success("用户登录成功",userModel);
    }

    @RequestMapping("/user/updatePassword")
    @ResponseBody
    public ResultInfo updateUserPwd(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
        //调用service层修改方法,从cookie中获取该userid值
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request),oldPassword,newPassword,confirmPassword);
        return success("密码更新成功");
    }


}
