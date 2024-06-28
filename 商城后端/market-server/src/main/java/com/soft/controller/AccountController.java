package com.soft.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.common.Result;
import com.soft.entity.Account;
import com.soft.service.AccountService;
import com.soft.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.soft.common.BaseController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController extends BaseController {
    @Autowired
    AccountService accountService;

    @GetMapping("/info/{accountId}")
    public Result info(@PathVariable String accountId){
        Account account = accountService.getById(accountId);
        return Result.success(account);
    }

    @GetMapping("/check/{accountId}")
    public Result check(@PathVariable String accountId){
        Account account = accountService.getById(accountId);
        if(account==null){
            return Result.success("手机号码可注册");
        }else{
            return Result.fail(20005,"手机号码已经注册","手机号码已经注册");
        }
    }

    @PostMapping("/register")
    public Result resgister(@RequestBody Account account){
        account.setCreated(LocalDateTime.now());
        account.setUpdated(LocalDateTime.now());
        account.setPassword(  MD5Utils.md5(MD5Utils.inputPassToNewPass(account.getPassword()) ) );  //将输入的密码先加工再加密
        account.setStatu(1);
        account.setAccountImg("https://server-system-soft.oss-cn-hangzhou.aliyuncs.com/default.jpg");
        accountService.save(account);
        return Result.success("用户信息注册成功");
    }
    //accountId=13333444444&password=123123
    @PostMapping("/login")
    public Result login(String accountId,String password){
        log.info("手机号:{}用户正在进行登录操作--",accountId);
        //查询accountId这个手机号在sys_account表中存在否?
        Account account = accountService.getOne(new QueryWrapper<Account>().eq("account_id", accountId));
        if(account==null){
            return Result.fail("账户的手机号码不存在");
        }else{
            //需要将输入的密码进行加工再加密
            String md5password = MD5Utils.md5(MD5Utils.inputPassToNewPass(password));
            //判断查询account账户中密码密文和输入密码密文相等
            if(account.getPassword().equals(md5password)){
                //如果密码正确，登录操作成功，判断账户是否被禁用    1正常 0禁用
                if(account.getStatu()==0){
                    return Result.fail("该账户被禁用不可用");
                }else{
                    //登录成功，直接返回登录账户对象信息
                    return Result.success(account);
                }
            }else{
                return Result.fail("密码不正确");
            }
        }
    }
}
