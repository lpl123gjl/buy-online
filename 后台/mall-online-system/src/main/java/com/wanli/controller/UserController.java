package com.wanli.controller;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.config.Const;
import com.wanli.entity.Role;
import com.wanli.entity.User;
import com.wanli.entity.UserBusiness;
import com.wanli.entity.UserRole;
import com.wanli.service.RoleService;
import com.wanli.service.UserBusinessService;
import com.wanli.service.UserRoleService;
import com.wanli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserBusinessService userBusinessService;


    //保存分配角色数据
    @PostMapping("/role/{id}")
    @Transactional
    public Result role(@PathVariable Long id,@RequestBody Long[] roleIds){
        //保存用户的角色信息，保存sys_user_role表
        //定义 关系表对象集合
        List<UserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId ->{
            UserRole ur = new UserRole();
            ur.setUserId(id);
            ur.setRoleId(roleId);

            userRoleList.add(ur);
        });

        //保存，先清除用户现有的角色关联信息
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",id));
        //录入重新设置的角色关联信息
        userRoleService.saveBatch(userRoleList);

        //用户的角色改变了，权限也就改变了，需要清除redis中该用户的权限字符串。
        User user = userService.getById(id);
        userService.clearUserAuthorityInfo(user.getUsername());  //清除redis中该用户名的权限
        return Result.success("角色分配成功");
    }
    @PostMapping("/business/{id}")
    @Transactional
    public Result business(@PathVariable Long id,@RequestBody Long[] businessIds){
        List<UserBusiness> userBusinessList = new ArrayList<>();
        Arrays.stream(businessIds).forEach(businessId ->{
            UserBusiness ur = new UserBusiness();
            ur.setUserId(id);
            ur.setBusinessId(businessId);

            userBusinessList.add(ur);
        });
        userBusinessService.saveBatch(userBusinessList);
        return Result.success("商家分配成功");
    }

    //重置密码
    @PostMapping("/repass/{id}")
    public Result repass(@PathVariable Long id){
        //根据用户id，查询该用户的详细的信息
        User user = userService.getById(id);
        //设置该用户密码为 默认密码（加密）。
        user.setPassword( passwordEncoder.encode( Const.DEFAULT_PASSWORD ) );  //设置密码为 666666
        user.setUpdated(LocalDateTime.now());  //用户的更新时间为当前时间

        //更新用户的数据
        userService.updateById(user);
        return Result.success("账户密码重置成功");
    }

    //根据用户id查询该用户信息
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        //查询出该id用户详细信息
        User user = userService.getById(id);
        //查询该用户的角色信息
        List<Role> roles = roleService.listRolesByUserId(user.getId());
        user.setRoles(roles);

        return Result.success(user);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@RequestBody User user){
        user.setCreated(LocalDateTime.now());
        if(StrUtil.isBlank(user.getAvatar())){
            user.setAvatar(Const.DEFAULT_AVATAR);  //设置用户头像为默认头像
        }

        //录入用户，密码是否有输入，如果没有输入，就取默认密码
        if(StrUtil.isBlank(user.getPassword())){
            //设置密码为默认密码
            user.setPassword(Const.DEFAULT_PASSWORD);
        }

        //页面提交明文，录入数据库中 用户密码需要SpringSecurity加密
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        userService.save(user);
        return Result.success("用户创建成功");
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@RequestBody User user){
        user.setUpdated(LocalDateTime.now());
        if(StrUtil.isBlank(user.getPassword())){
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
        }
        userService.updateById(user);
        return Result.success("用户数据更新成功");
    }

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list(String name){
        //Page<Role> page = roleService.page(getPage(),new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        Page<User> page = userService.page(getPage(), new QueryWrapper<User>().like(StrUtil.isNotBlank(name), "username", name));

        //查询出user分页数据，user对象中没有roles是null。
        page.getRecords().forEach(u->{
            u.setRoles(roleService.listRolesByUserId(u.getId()));
        });

        return Result.success(page);
    }

    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestBody Long[] ids){
        //同步删除  用户和角色的关系 sys_user_role
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id",ids));
        //删除用户
        userService.removeByIds(Arrays.asList(ids));
        return Result.success("删除用户操作成功");
    }
    @GetMapping("/count")
    public Result count(){
        return Result.success(userService.count());
    }
}














