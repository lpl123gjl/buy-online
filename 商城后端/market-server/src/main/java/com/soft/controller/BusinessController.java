package com.soft.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.common.Result;
import com.soft.entity.Business;
import com.soft.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.soft.common.BaseController;

import java.util.List;

@RestController
@RequestMapping("/business")
public class BusinessController extends BaseController {
    @Autowired
    BusinessService businessService;

    @GetMapping("/list")
    public Result list(){
        List<Business> list = businessService.list();
        if(list==null){
            return Result.fail("首页商家数据加载失败");
        }else{
            return Result.success(list);
        }
    }

    @GetMapping("/listByCategoryId/{categoryId}")
    public Result listByCategoryId(@PathVariable Integer categoryId){
        QueryWrapper<Business> qw = new QueryWrapper<>();
        qw.eq("category_id",categoryId);
        List<Business> list = businessService.list(qw); //该类别编号下的商家集合信息
        if(list==null){
            return Result.fail("该类别下的商家数据加载失败");
        }else{
            return Result.success(list);
        }
    }

    @GetMapping("/info/{businessId}")
    public Result info(@PathVariable Long businessId){
        //如果是根据主键查询
        Business business = businessService.getById(businessId);
        if(business==null){
            return Result.fail("商家的详细信息加载失败.");
        }else{
            return Result.success(business);
        }
    }
}
