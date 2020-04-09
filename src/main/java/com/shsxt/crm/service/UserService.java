package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService  extends BaseService<User,Integer>{

    @Resource
    private UserMapper userMapper;

    public UserModel queryUserByNameAndPwd(String userName, String userPwd){
        /**
         *  1.参数判断:
         *      用户名 非空
         *      密码   非空
         *  2.根据前台用户名和密码查询该用户
         *  3.校验返回用户是否存在
         *       存在:
         *          校验密码:
         *               正确,登入成功
         *               失败,账号或密码密码失败,返回给前台
         *       不存在:
         *          登入失败,返回失败信息
         *
         */
        //校验是否为空
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByName(userName);
        //判断用户名是否存在
        AssterUtil.isTrue(null==user,"该用户不存在,请先注册一个吧!");
        //判断密码是否错误
        AssterUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"密码错误,请重试!");
        //返回封装好的UserModel数据
        return buildUserModelInfo(user);
    }

    /**
     *  简化查询后返回的数据属性值,以及将id值进行加密
     * @param user
     * @return
     */
    private UserModel buildUserModelInfo(User user) {
        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName(),user.getUserPwd());
    }

    /**
     *  判断用户是否为空
     * @param userName
     * @param userPwd
     */
    public void checkLoginParams(String userName,String userPwd){
        AssterUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        AssterUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空!");
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param reconfirmPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPwd,String newPwd,String reconfirmPwd){
        /**
         *  1.参数校验:
         *      用户id      非空,必须存在
         *      原始密码    非空
         *      新密码     非空
         *      确认密码   非空
         *      新密码不能与原始密码相同
         *  2.设置密码  密码加密
         *  3.执行更新
         */
         checkPasswordParams(userId,oldPwd,newPwd,reconfirmPwd);
        User user = selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newPwd));
        AssterUtil.isTrue(updateByPrimaryKeySelective(user)<1,"更新失败!");
    }

    private void checkPasswordParams(Integer userId, String oldPwd, String newPwd, String reconfirmPwd) {
        User user = selectByPrimaryKey(userId);
        AssterUtil.isTrue(null==userId || null== user,"该用户未登入或不存在!");
        AssterUtil.isTrue(StringUtils.isBlank(oldPwd),"请输入原始密码!");
        AssterUtil.isTrue(StringUtils.isBlank(newPwd),"请输入新密码!");
        AssterUtil.isTrue(StringUtils.isBlank(reconfirmPwd),"请输入确认密码!");
        AssterUtil.isTrue(!(newPwd.equals(reconfirmPwd)),"新密码与确认密码不一致!");
        AssterUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同!");
        //判断输入的原始密码是否与原始密码一致
        AssterUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPwd))),"原始密码错误,请重新输入!");
    }
}
