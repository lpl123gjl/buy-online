package com.wanli.controller;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.entity.Menu;
import com.wanli.entity.Role;
import com.wanli.entity.RoleMenu;
import com.wanli.entity.UserRole;
import com.wanli.service.RoleMenuService;
import com.wanli.service.RoleService;
import com.wanli.service.UserRoleService;
import com.wanli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;


    @GetMapping("/tree")
    public Result tree(){
        List<Role> list = roleService.list();
        return Result.success(list);
    }

    @PostMapping("/perms/{roleId}")
    @Transactional
    public Result perms(@PathVariable Long roleId,@RequestBody Long[] menuIds){
        //创建sys_role_menu关联表的 集合对象
        List<RoleMenu> roleMenuList = new ArrayList<>();
        Arrays.stream(menuIds).forEach(mid->{
            RoleMenu rm =new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(mid);
            roleMenuList.add(rm);
        });

        //删除掉该角色 之前所有的关联表中的权限信息
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        //直接批量保存 关联表集合对象
        roleMenuService.saveBatch(roleMenuList);

        //清除与该角色 相关的所有的用户的redis权限字符串信息
        userService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.success("角色权限信息保存成功");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        //查询参数id的角色的信息
        Role role = roleService.getById(id);

        List<RoleMenu> roleMenuList = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
        //循环关联表 取出roleMenu对象中menuId，形成一个新的集合。这个集合就是 树形控件显示权限。
        List<Long> menuIds = roleMenuList.stream().map(rm -> rm.getMenuId()).collect(Collectors.toList());

        role.setMenuIds(menuIds);
        return Result.success(role);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        role.setCreated(LocalDateTime.now());
        roleService.save(role);
        return Result.success("录入角色信息成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Role role){
        //设置菜单更新时间：
        role.setUpdated(LocalDateTime.now());
        roleService.updateById(role);
        return Result.success("角色更新成功");
    }

    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestBody Long[] ids){
        //同步删除 sys_user_role表
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id",ids) );
        //同步删除 sys_role_menu表
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id",ids));
        //删除相关id的角色。
        roleService.removeByIds( Arrays.asList(ids) );

        return Result.success("删除角色操作成功");
    }

    @GetMapping("/list")
    public Result list(String name){
        //该方法不需要定义接收分页的参数，因为BaseController中接收了，并且封装成Page，只需要调用getPage()
        //MyBatisPlus  page()分页查询
        //Page<Role> page = roleService.page(getPage());
        //返回Page对象中，包含records属性，该属性中包括 分页角色集合，还有其它相关的分页参数。

        //name 如果有值，使用模糊查询 查询分页表格数据。 如果没有值，就添加模糊查询。
        Page<Role> page = roleService.page(getPage(),new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        return Result.success(page);
    }
}
