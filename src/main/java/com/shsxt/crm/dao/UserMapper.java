package com.shsxt.crm.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.vo.User;

import java.util.Map;


public interface UserMapper extends BaseMapper<User,Integer> {
   public User queryUserByName(String userName);

    public  User selectByParams(UserQuery userQuery);
}