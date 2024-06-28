package com.wanli.service;

import com.wanli.entity.Role;
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
public interface RoleService extends IService<Role> {
    //根据用户id，查询该用户所具备角色信息
    public List<Role> listRolesByUserId(Long userId);
}
