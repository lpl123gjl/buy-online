package com.wanli.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.Menu;
import com.wanli.entity.Role;
import com.wanli.entity.User;
import com.wanli.mapper.MenuMapper;
import com.wanli.mapper.UserMapper;
import com.wanli.service.MenuService;
import com.wanli.service.RoleService;
import com.wanli.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanli.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",username);
        User user = userMapper.selectOne(qw);
        return user;
    }

    //构建 一个有规则的权限字符串。
    @Override
    public String getUserAuthorityInfo(Long userId) {
        //可以调用userMapper.getNavMenuIds(用户编号)可以获得该用户所能操作的菜单id。
        String authorityString = "";
        //存储到Redis中key，"granted"+当前登录用户名
        User user = this.getById(userId);
        String key = "granted"+user.getUsername();

        //判断，redis中如果已经有权限字符串，就直接取
        if(redisUtil.hasKey(key)){
            authorityString = (String)redisUtil.get(key);
        }else{
            //查询当前userId用户所具备的角色信息
            QueryWrapper role_qw = new QueryWrapper();
            role_qw.inSql("id","select role_id from sys_user_role where user_id="+userId);
            List<Role> roles = roleService.list(role_qw);

            //ROLE_normal,ROLE_admin
            if(roles.size()>0){
                String roleString = roles.stream().map(r->"ROLE_"+r.getCode()).collect(Collectors.joining(","));
                authorityString = roleString+",";  //权限字符串具备：ROLE_normal,ROLE_admin,
            }

            //查询当前userId用户能够操作哪些菜单
            List<Long> menusIds = userMapper.getNavMenuIds(userId);
            //根据 菜单的编号，查询该菜单的详细数据。
            List<Menu> menus = menuService.listByIds(menusIds);

            if(menus.size() > 0 ){
                String perms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                //sys:menu:list,sys:role:list,sys:user:save,.....
                authorityString=authorityString.concat(perms);
                //权限字符串存储：ROLE_normal,ROLE_admin,sys:menu:list,sys:role:list,sys:user:save.....
                log.info("权限字符串:{}",authorityString);
            }

            //将产生权限字符串，存储到redis中
            redisUtil.set(key,authorityString);
        }

        return authorityString;
    }




    //根据删除menuId 清除该用户与该菜单关联权限信息:
    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        //删除58菜单，查询出与58菜单编号 相关的所有的用户。
        List<User> users = userMapper.listByMenuId(menuId);
        //清除查询出的用户的redis权限，因为这些用户的权限是和删除菜单相关的
        users.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    //根据用户名，清除redis中存储该用户权限字符串
    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("granted"+username);
    }

    //根据删除角色权限，清除与该角色相关的所有的用户的权限信息
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        //查找和该角色相关的所有用户。
        List<User> list = this.list(new QueryWrapper<User>().inSql("id", "select user_id from sys_user_role where role_id=" + roleId));
        //循环找出 用户集合，取出每个用户的username 组成redis 存储权限key，进行删除
        list.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
