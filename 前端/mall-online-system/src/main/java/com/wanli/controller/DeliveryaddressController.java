package com.wanli.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.entity.Deliveryaddress;
import com.wanli.entity.Role;
import com.wanli.entity.User;
import com.wanli.service.AccountService;
import com.wanli.service.DeliveryaddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Byterain
 * @since 2024-06-18
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryaddressController extends BaseController {
    @Autowired
    DeliveryaddressService deliveryaddressService;
    @Autowired
    AccountService accountService;

    @GetMapping("/list/{accountId}")
    public Result list(@PathVariable String accountId){
        //该方法不需要定义分页的参数,因为BaseController中接收了，并且封装成Page，只需要调用getPage（）
//        Page<Role> page = roleService.page(getPage());
        //如果name有值，添加模糊查询条件，使用模糊查询分页的表格数据，如果没有值，就不添加
        Page<Deliveryaddress> page = deliveryaddressService.page(getPage(),new QueryWrapper<Deliveryaddress>().like(StrUtil.isNotBlank(accountId),"account_id",accountId));
        page.getRecords().forEach(u->{
            u.setAccountName(accountService.getAccountNameByAccountId(u.getAccountId()));
        });
        return Result.success(page);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Deliveryaddress deliveryaddress){
        deliveryaddress.setCreated(LocalDateTime.now());
        deliveryaddress.setUpdated(null);
        if(accountService.getById(deliveryaddress.getContactTel())==null){
            return Result.fail("不存在该账户信息");
        }
        deliveryaddress.setAccountId(deliveryaddress.getContactTel());
        deliveryaddressService.save(deliveryaddress);
        return Result.success("录入角色信息成功");
    }


    @PostMapping("/update")
    public Result update(@RequestBody Deliveryaddress deliveryaddress){
        deliveryaddress.setUpdated(LocalDateTime.now());
        deliveryaddressService.updateById(deliveryaddress);
        return Result.success("角色更新成功");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        Deliveryaddress deliveryaddress = deliveryaddressService.getById(id);
        return Result.success(deliveryaddress);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
        deliveryaddressService.removeByIds(Arrays.asList(ids));
        return Result.success("删除成功");
    }
}
