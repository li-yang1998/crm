package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("user/index")
    public String index(){
        return "user";
    }

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

    @RequestMapping("/user/list")
    @ResponseBody
    public Map<String,Object> queryUser(UserQuery userQuery){
        return userService.queryUser(userQuery);
    }

    @RequestMapping("/user/save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("添加成功!");
    }

    @RequestMapping("/user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer id){
        userService.deleteUser(id);
        return success("删除成功!");
    }

    @RequestMapping("/user/update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("更新成功!");
    }
}
