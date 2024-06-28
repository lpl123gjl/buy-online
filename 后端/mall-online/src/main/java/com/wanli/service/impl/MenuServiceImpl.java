package com.wanli.service.impl;

import com.wanli.entity.Menu;
import com.wanli.entity.User;
import com.wanli.entity.dto.MenuDto;
import com.wanli.mapper.MenuMapper;
import com.wanli.mapper.UserMapper;
import com.wanli.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2024-06-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Override
    public List<MenuDto> getCurrentUserNav(String username) {
        User user = userService.getUserByUsername(username);
        List<Long> menuIds = userMapper.getNavMenuIds(user.getId());

        List<Menu> menus = this.listByIds(menuIds);
        List<Menu> finalMenu = buildTreeMenu(menus);

        return convert(finalMenu);
    }
    //查询菜单管理界面的表格数据
    @Override
    public List<Menu> tree() {
        List<Menu> list = this.list();
        List<Menu> treeMen = this.buildTreeMenu(list);
        return treeMen;
    }


    private List<Menu> buildTreeMenu(List<Menu> menus){
        List<Menu> finalMenu = new ArrayList<>();
        for(Menu m : menus){
            for(Menu e : menus){
                if(e.getParentId() == m.getId()){
                    m.getChildren().add(e);
                }
            }
            if(m.getParentId() == 0L){
                finalMenu.add(m);
            }
        }
        return finalMenu;
    }

    private List<MenuDto> convert(List<Menu> menus){
        List<MenuDto> menuDtoList = new ArrayList<>();
        menus.forEach(m -> {
            MenuDto dto = new MenuDto();
            dto.setId(m.getId());
            dto.setTitle(m.getName());
            dto.setName(m.getPerms());
            dto.setIcon(m.getIcon());
            dto.setPath(m.getPath());
            dto.setComponent(m.getComponent());
            if(m.getChildren().size()>0){
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtoList.add(dto);
        });
        return menuDtoList;
    }
}
