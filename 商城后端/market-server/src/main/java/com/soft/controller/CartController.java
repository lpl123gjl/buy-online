package com.soft.controller;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.common.Result;
import com.soft.entity.Business;
import com.soft.entity.Cart;
import com.soft.entity.Goods;
import com.soft.service.BusinessService;
import com.soft.service.CartService;
import com.soft.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.soft.common.BaseController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController {
    @Autowired
    CartService cartService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    BusinessService businessService;


    @GetMapping("/list/{accountId}")
    public Result list(@PathVariable String accountId) {
        return Result.success(cartService.getBusinessesWithGoodsByAccountId(accountId));
    }
    @GetMapping("/listCart/{accountId}")
    public Result listCart2(@PathVariable String accountId){
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq("account_id",accountId);

        List<Cart> list = cartService.list(qw);

        //因为list中查询出的购物车数据只有 goods_id，quantity，需要将goods_id带入到sys_goods查询商品详细信息
        list.stream().forEach(cart -> {
            Goods goods = goodsService.getById(cart.getGoodsId());//使用商品goods_id主键查询，getById()
            cart.setGoods(goods);  //将查询出的商品详情对象 存入到购物车类中goods变量中。
        });

        list.stream().forEach(cart->{
            Business business = businessService.getById(cart.getBusinessId());
            cart.setBusiness(business);
        });
        return Result.success(list);
    }


    @GetMapping("/listCart/{accountId}/{businessId}")
    public Result listCart(@PathVariable String accountId,@PathVariable Long businessId){
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq("account_id",accountId);
        qw.eq( businessId!=0, "business_id",businessId);

        List<Cart> list = cartService.list(qw);

        //因为list中查询出的购物车数据只有 goods_id，quantity，需要将goods_id带入到sys_goods查询商品详细信息
        list.stream().forEach(cart -> {
            Goods goods = goodsService.getById(cart.getGoodsId());//使用商品goods_id主键查询，getById()
            cart.setGoods(goods);  //将查询出的商品详情对象 存入到购物车类中goods变量中。
        });

        list.stream().forEach(cart->{
            Business business = businessService.getById(cart.getBusinessId());
            cart.setBusiness(business);
        });
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Cart cart){
        cart.setCreated(LocalDateTime.now());
        cart.setUpdated(LocalDateTime.now());
        cart.setStatu(1);
        cartService.save(cart);
        return Result.success("购物车保存成功");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Cart cart){
        cart.setUpdated(LocalDateTime.now());
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq(ObjectUtil.isNotEmpty(cart.getGoodsId()),"goods_id",cart.getGoodsId());
        qw.eq(ObjectUtil.isNotEmpty(cart.getBusinessId()),"business_id",cart.getBusinessId());
        qw.eq(StrUtil.isNotBlank(cart.getAccountId()),"account_id",cart.getAccountId());
        cartService.update(cart,qw);
        return Result.success("购物车更新成功");
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody Cart cart){
        QueryWrapper<Cart> qw = new QueryWrapper<>();
        qw.eq(ObjectUtil.isNotEmpty(cart.getGoodsId()),"goods_id",cart.getGoodsId());
        qw.eq(ObjectUtil.isNotEmpty(cart.getBusinessId()),"business_id",cart.getBusinessId());
        qw.eq(StrUtil.isNotBlank(cart.getAccountId()),"account_id",cart.getAccountId());
        cartService.remove(qw);
        return Result.success("购物车删除成功");
    }
}
