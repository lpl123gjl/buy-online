package com.wanli.mapper;

import com.wanli.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Byterain
 * @since 2024-05-28
 */
public interface UserMapper extends BaseMapper<User> {
    public List<Long> getNavMenuIds(Long userId);
    //查询 与删除菜单id 权限相关的所有用户数据
    public List<User> listByMenuId(Long menuId);
}
