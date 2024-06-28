package com.wanli.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.common.Result;
import com.wanli.entity.Orders;
import com.wanli.entity.Ordersdetailet;
import com.wanli.entity.UserBusiness;
import com.wanli.entity.UserRole;
import com.wanli.entity.dto.OrderGoodsDto;
import com.wanli.mapper.OrdersMapper;
import com.wanli.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.wanli.common.BaseController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
@RequestMapping("/orders")
public class OrdersController extends BaseController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    BusinessService businessService;
    @Autowired
    DeliveryaddressService deliveryaddressService;
    @Autowired
    OrdersdetailetService ordersdetailetService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrdersMapper ordersMapper;
    @GetMapping("/list")
    public Result allOrders(Long orderId){
        //分页
        Page<Orders> page = ordersService.page(getPage(), new QueryWrapper<Orders>().eq(orderId != null, "order_id", orderId)
        );
        page.getRecords().forEach(o->{
            o.setBusiness(businessService.getById(o.getBusinessId()));
            o.setDeliveryaddress(deliveryaddressService.getById(o.getDaId()));
            List<Ordersdetailet> odList = ordersdetailetService.list(new QueryWrapper<Ordersdetailet>().eq("order_id",o.getOrderId()));
            odList.forEach(od->{
                od.setGoods(goodsService.getById(od.getGoodsId()));
            });
            o.setOrdersDetailets(odList);
        });


        return Result.success(page);
    }
    @GetMapping("/one")
    //通过business_id查询当前商家的所有订单,分页还按照order_id进行模糊查询
    public Result one(@RequestParam String businessIds, Long orderId){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<Orders>().in("business_id", Arrays.asList(businessIds.split(",")));
        if(orderId!=null){
            queryWrapper.eq("order_id",orderId);
        }
        Page<Orders> page = ordersService.page(getPage(),queryWrapper);
                page.getRecords().forEach(o->{
                    o.setBusiness(businessService.getById(o.getBusinessId()));
                    o.setDeliveryaddress(deliveryaddressService.getById(o.getDaId()));
                    List<Ordersdetailet> odList = ordersdetailetService.list(new QueryWrapper<Ordersdetailet>().eq("order_id",o.getOrderId()));
                    odList.forEach(od->{
                        od.setGoods(goodsService.getById(od.getGoodsId()));
                    });
                    o.setOrdersDetailets(odList);
                });
                return Result.success(page);
    }
    @GetMapping("/rank/{businessId}")
    public Result test(@PathVariable Long businessId){
        return Result.success(ordersMapper.getOrderGoodsSummaryByBusinessId(businessId));
    }


    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestBody Long[] ids){

        ordersService.remove(new QueryWrapper<Orders>().in("order_id",ids));

        ordersdetailetService.remove(new QueryWrapper<Ordersdetailet>().in("order_id",ids));
        return Result.success("删除订单操作成功");
    }
    @GetMapping("/count/{timeRange}")
    public Result count(@PathVariable String timeRange){
        //统计不同商家的订单总数并按照订单数排序
        return Result.success(ordersService.getOrdersCount(timeRange));
    }
    @GetMapping("/sales/{timeRange}")
    public Result sales(@PathVariable String timeRange){
        //统计不同商家的订单销售额并按照订单销售额排序
        return Result.success(ordersService.getBusinessSalesRank(timeRange));
    }
    @GetMapping("/allcount")
    public Result allcount() {
        return Result.success(ordersService.count(new QueryWrapper<Orders>().eq("state",1)));
    }
}
