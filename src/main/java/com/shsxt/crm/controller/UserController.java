package com.shsxt.crm.controller;



import com.shsxt.base.BaseController;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
public class UserController{

    @Autowired
    private UserService userService;

    @RequestMapping("user/queryUserById")
    @ResponseBody
    public User queryUserById(Integer userId){
        return userService.selectByPrimaryKey(userId);
    }


}
