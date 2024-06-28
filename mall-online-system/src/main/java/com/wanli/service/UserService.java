package com.wanli.service;

import com.wanli.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Byterain
 * @since 2024-05-28
 */
public interface UserService extends IService<User> {
    public User getUserByUsername(String username);
    public String getUserAuthorityInfo (Long userId);
    //根据删除menuId 清除该用户与该菜单关联权限信息:
    public void clearUserAuthorityInfoByMenuId(Long menuId);
    public void clearUserAuthorityInfo(String username);
    public void clearUserAuthorityInfoByRoleId(Long roleId);

}
