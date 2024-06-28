package com.wanli.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanli.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Byterain
 * @since 2024-06-21
 */
public interface AccountService extends IService<Account> {

    public String getAccountNameByAccountId(String accountId);
}
