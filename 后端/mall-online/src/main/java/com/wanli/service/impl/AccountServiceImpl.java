package com.wanli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.Account;
import com.wanli.entity.Role;
import com.wanli.mapper.AccountMapper;
import com.wanli.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Byterain
 * @since 2024-06-21
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    AccountService accountService;
    @Override
    public String getAccountNameByAccountId(String accountId) {
        Account account = this.getById(accountId);
        return account.getAccountName();
    }
}
