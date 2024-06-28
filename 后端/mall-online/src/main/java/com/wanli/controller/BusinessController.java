package com.wanli.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.entity.Business;
import com.wanli.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @author hzy
 * @since 2024-06-21
 */
@RestController
@RequestMapping("/business")
public class BusinessController extends BaseController {
    @Autowired
    BusinessService businessService;

    @GetMapping("/getlist")
    public Result getList(){

        return Result.success(businessService.list());
    }
    @GetMapping("/count")
    public Result count()
    {
        return Result.success(businessService.count());
    }



    @GetMapping("/tree")
    public Result getDifferentBusinessIdList() {
        return Result.success(businessService.getDifferentBusinessIdList());
    }

    @Transactional
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] businessIds){
        //删除相关Id的角色
        businessService.removeByIds(Arrays.asList(businessIds));
        //TODO:删除商家相关的信息也要删除商家旗下的商品信息
        return Result.success("删除成功");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id){
        //查询商家id对应的信息
        Business business =businessService.getById(id);
        return Result.success(business);
    }

    @GetMapping("/list")
    public Result list(String name){
        //Page<Role> page= roleService.page(getPage(),new QueryWrapper<Role>().like(StrUtil.isNotBlank(name),"name",name));
        Page<Business> page=businessService.page(getPage(),new QueryWrapper<Business>().like(StrUtil.isNotBlank(name),"business_name",name));
        return Result.success(page);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Business business){
        business.setCreated(LocalDateTime.now());
        businessService.save(business);
        return Result.success("商家信息录入成功");
    }
    @PostMapping("/update")
    public Result update(@RequestBody Business business){
        business.setUpdated(LocalDateTime.now());
        businessService.updateById(business);
        return Result.success("商家信息更新成功");
    }
}
