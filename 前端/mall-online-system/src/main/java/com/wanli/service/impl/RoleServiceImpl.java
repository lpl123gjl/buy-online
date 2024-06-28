package com.wanli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.Role;
import com.wanli.mapper.RoleMapper;
import com.wanli.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Byterain
 * @since 2024-05-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<Role> listRolesByUserId(Long userId) {
        List<Role> list = this.list(new QueryWrapper<Role>().inSql("id","select role_id from sys_user_role where user_id="+userId));
        return list;
    }
}
