package com.wanli.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.config.Const;
import com.wanli.entity.Account;
import com.wanli.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public Result list(String accountName){
        Page<Account> page = accountService.page(getPage(),new QueryWrapper<Account>().like(StrUtil.isNotBlank(accountName),"account_name",accountName));
        return Result.success(page);
    }

    @GetMapping("/info/{accountId}")
    public Result info(@PathVariable String accountId){
        Account account = accountService.getById(accountId);
        return Result.success(account);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Account account){
        account.setCreated(LocalDateTime.now());
        if(StrUtil.isBlank(account.getAccountImg())){
            account.setAccountImg(Const.USER_AVATAR);
        }
        //
        if(StrUtil.isBlank(account.getPassword())){
            //设置密码为默认密码
            account.setPassword(Const.DEFAULT_PASSWORD);
        }

        //页面提交明文，录入数据库需要密文
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);

        accountService.save(account);
        return Result.success("用户创建成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Account account){
        account.setUpdated(LocalDateTime.now());
        if(StrUtil.isBlank(account.getPassword())){
            String password = passwordEncoder.encode(account.getPassword());
            account.setPassword(password);
        }
        accountService.updateById(account);
        return Result.success("数据用户更新成功");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        accountService.removeByIds(Arrays.asList(ids));
        return Result.success("删除成功");
    }

    @PostMapping("/repass/{accountId}")
    public Result repass(@PathVariable String accountId){
        //根据用户id，查询该用户的详细的信息
        Account account = accountService.getById(accountId);
        //设置改密码为默认密码
        account.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
        account.setUpdated(LocalDateTime.now());

        accountService.updateById(account);
        return Result.success("账户密码重置成功");
    }
    @GetMapping("/count")
    public Result count(){
        return Result.success(accountService.count());
    }
}
