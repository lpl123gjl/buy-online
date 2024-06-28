package com.wanli.controller;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.common.Result;
import com.wanli.entity.Menu;
import com.wanli.entity.RoleMenu;
import com.wanli.entity.User;
import com.wanli.entity.dto.MenuDto;
import com.wanli.service.MenuService;
import com.wanli.service.RoleMenuService;
import com.wanli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    @GetMapping("/list")
    public Result list(){
        List<Menu> tree = menuService.tree();
        return Result.success(tree);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @PostMapping("/delete/{id}")
    @Transactional
    public Result delete(@PathVariable Long id){
        //删除菜单下面存在子菜单，查询删除菜单下面所有的子菜单。
        int count = menuService.count(new QueryWrapper<Menu>().eq("parent_id", id));
        if(count>0){
            return Result.fail("请先删除该菜单下面的子菜单数据");
        }else{
            //Menu（删除菜单58）---sys_role_menu（删除 6  58）----Role----User(byterain用户具备6角色)
            //清楚redis中存储当前登录用户权限字符串。重置用户权限。
            userService.clearUserAuthorityInfoByMenuId(id);


            //菜单和角色关联，所有还需要删除 sys_role_menu
            roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id));
            //删除菜单
            menuService.removeById(id);

            return Result.success("菜单删除成功");
        }
    }

    @PostMapping("/save")
    public Result save(@RequestBody Menu menu){
        //设置新建菜单的创建时间。
        menu.setCreated(LocalDateTime.now());
        menuService.save(menu);
        return Result.success("菜单保存成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Menu menu){
        //设置菜单更新时间：
        menu.setUpdated(LocalDateTime.now());
        menuService.updateById(menu);
        return Result.success("菜单更新成功");
    }

    @GetMapping("/nav")
    public Result nav(Principal principal){
        //直接从SpringSecurity封装的对象中取 登录用户名
        String username = principal.getName();
        //获得当前登录用户菜单的权限数据。
        List<MenuDto> menuDtoList = menuService.getCurrentUserNav(username);

        //查询当前登录用户权限数据
        User user = userService.getUserByUsername(username);
        //查询当前用户的权限字符串
        String userAuthorityInfo = userService.getUserAuthorityInfo(user.getId());
        //将权限字符串 按照逗号分隔符 转化为数组。
        String[] authoritys = StringUtils.tokenizeToStringArray(userAuthorityInfo, ",");

        //{nav:xxxxxx,authoritys:xxxxx}
        return Result.success(MapUtil.builder().put("nav",menuDtoList).put("authoritys",authoritys).map());
    }
}
