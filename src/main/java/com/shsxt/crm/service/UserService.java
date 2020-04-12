package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.utils.AssterUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService  extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user){
        /**
         *  参数校验:
         *      手机号码   非空，符合手机号码格式要求
         *      Emil邮件  非空，符合邮件格式要求
         *      姓名      非空，唯一性
         *      真实姓名  非空
         *
         *  参数默认值设置:
         *      密码默认                123456，加密
         *      创建时间 与 更新时间     当前系统时间
         *      is_valid                1
         *   执行注册,判断结果
         *
         */
        checkUserParams(user.getUserName(),user.getEmail(),user.getTrueName(),user.getPhone());
        user.setUserPwd("123456");
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        AssterUtil.isTrue(insertHasKey(user)==null,"注册失败!");
        /**
         *  用户角色分配
         */
        Integer userId = user.getId();
        relaionUserRole(userId,user.getRoleIds());


    }

    private void relaionUserRole(Integer userId, List<Integer> roleIds) {
        int count = userRoleMapper.selectCountByUserId(userId);
        if (count > 0) {
            AssterUtil.isTrue(userRoleMapper.deleteByUserId(userId) < count, "用户分配角色失败!");
        }

        if(null!=userId&&roleIds.size()>0){
            List<UserRole> userRoles = new ArrayList<>();
            roleIds.forEach(roleId->{
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setUpdateDate(new Date());
                userRole.setCreateDate(new Date());
                userRoles.add(userRole);
            });
            AssterUtil.isTrue(userRoleMapper.insertBatch(userRoles)<1,"用户角色分配失败!");
        }
    }


    public Map<String,Object> queryUser(UserQuery userQuery){
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        Map<String,Object> map = new HashMap<>();
        PageInfo<User> pageInfo = new PageInfo<>(selectByParams(userQuery));
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }

    private void checkUserParams(String userName, String email, String trueName, String phone) {
        AssterUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssterUtil.isTrue(StringUtils.isBlank(email),"邮件地址不能为空");
        AssterUtil.isTrue(StringUtils.isBlank(trueName),"真实姓名不能为空");
        AssterUtil.isTrue(StringUtils.isBlank(phone),"电话号码不能为空");
        //数据库中查找是否唯一
        AssterUtil.isTrue(null!=(userMapper.queryUserByName(userName)),"该用户名已存在,请换个用户名!");
        //校验手机号码是否符合正常格式要求
        AssterUtil.isTrue(!(PhoneUtil.isMobile(phone)),"手机号码格式不正确");
        //验证邮箱地址格式
        AssterUtil.isTrue(!(PhoneUtil.isNameAddressFormat(email)),"邮箱地址格式不正确");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer id){
        /**
         *  参数校验:
         *      id  非空
         *      id  必须存在
         *      执行删除,判断结果
         */
        User user = selectByPrimaryKey(id);
        AssterUtil.isTrue( null==user || null==id,"暂无该记录,请先添加!");
        int count = userRoleMapper.selectCountByUserId(id);
        //先查找用户与权限表中的数据，并删除
        if (count > 0) {
            AssterUtil.isTrue(userRoleMapper.deleteByUserId(id) < count, "用户分配角色失败!");
        }

        AssterUtil.isTrue(deleteByPrimaryKey(id)<1,"删除失败,请稍后重试!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        /**
         *  参数校验:
         *      手机号码   非空，符合手机号码格式要求
         *       Emil邮件  非空，符合邮件格式要求
         *      姓名      非空，并该姓名在数据库中存在
         *      真实姓名  非空
         *
         */
        checkUserParams(user.getUserName(), user.getEmail(), user.getTrueName(), user.getPhone());
        user.setUpdateDate(new Date());
        AssterUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "更新失败,请稍后重试!");
        /**
         *  分配角色:
         *      用户角色已存在,清空该用户所有角色
         *      添加分配后的角色到数据库中
         */

        //获取roleIds,遍历添加到userRole中在添加到list集合中,在进行批量添加
        relaionUserRole(user.getId(),user.getRoleIds());

    }
}
