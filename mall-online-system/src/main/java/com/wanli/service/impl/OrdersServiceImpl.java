package com.wanli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.Business;
import com.wanli.entity.Orders;
import com.wanli.entity.dto.BusinessDto;
import com.wanli.mapper.OrdersMapper;
import com.wanli.service.BusinessService;
import com.wanli.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    BusinessService businessService;

    @Override public List<BusinessDto> getOrdersCount(String timeRange) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("business_id", "COUNT(*) AS orders_count");
        // 添加状态筛选条件
        queryWrapper.eq("state", 1);

        // 根据时间范围动态添加时间筛选条件
        LocalDateTime now = LocalDateTime.now();
        switch (timeRange) {
            case "ONE_YEAR":
                queryWrapper.ge("created", now.minusYears(1)); // 一年前
                break;
            case "ONE_MONTH":
                queryWrapper.ge("created", now.minusMonths(1)); // 一月前
                break;
            case "SEVEN_DAYS":
                queryWrapper.ge("created", now.minusDays(7)); // 七天前

                break;
            default:

                break;
        }

        // 分组和排序保持不变
        queryWrapper.groupBy("business_id").orderByDesc("orders_count");

        List<BusinessDto> businessDtoList = new ArrayList<>();
        for (Orders orders : this.list(queryWrapper)) {
            BusinessDto businessDto = new BusinessDto();
            Business business = businessService.getById(orders.getBusinessId());

            // 确保Orders实体中有getOrdersCount方法，如果没有，需要根据实际情况调整
            if (business != null) {
                businessDto.setBusinessId(business.getBusinessId());
                businessDto.setBusinessName(business.getBusinessName());
                businessDto.setBusinessExplain(business.getBusinessExplain());
                businessDto.setCreated(business.getCreated());
                businessDto.setOrdersCount(orders.getOrdersCount());
                businessDtoList.add(businessDto);
            }
        }

        return businessDtoList;
    }
    @Override
    public List<BusinessDto> getBusinessSalesRank(String timeRange) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>(); queryWrapper.select("business_id", "SUM(order_total) AS sales");
        // 添加状态筛选条件
        queryWrapper.eq("state", 1);

        // 根据时间范围动态添加时间筛选条件
        LocalDateTime now = LocalDateTime.now();
        switch (timeRange) {
            case "ONE_YEAR":
                queryWrapper.ge("created", now.minusYears(1)); // 一年前
                break;
            case "ONE_MONTH":
                queryWrapper.ge("created", now.minusMonths(1)); // 一月前
                break;
            case "SEVEN_DAYS":
                queryWrapper.ge("created", now.minusDays(7)); // 七天前

                break;
            default:

                break;
        }

        // 分组和排序保持不变
        queryWrapper.groupBy("business_id").orderByDesc("sales");
        List<BusinessDto> businessDtoList = new ArrayList<>();
        for (Orders orders : this.list(queryWrapper)) {
            BusinessDto businessDto = new BusinessDto();
            Business business = businessService.getById(orders.getBusinessId());
            businessDto.setBusinessId(orders.getBusinessId());
            businessDto.setBusinessName(business.getBusinessName());
            businessDto.setBusinessExplain(business.getBusinessExplain());
            businessDto.setCreated(business.getCreated());
            // 将BigDecimal类型的sales格式化为两位小数，并补0
            BigDecimal sales = orders.getSales();
            DecimalFormat df = new DecimalFormat("0.00");
            df.format(sales);
            businessDto.setSales(String.valueOf(sales));
            businessDtoList.add(businessDto);
        }

        return businessDtoList;
    }
}
