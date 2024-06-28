package com.wanli.service;

import com.wanli.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanli.entity.dto.MenuDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzy
 * @since 2024-06-03
 */
public interface MenuService extends IService<Menu> {
    //获得系统HOME页面菜单的数据： 不同用户，不同角色，不同菜单权限。不同用户看到不同菜单。
    public List<MenuDto> getCurrentUserNav(String username);
    public List<Menu> tree();

}
