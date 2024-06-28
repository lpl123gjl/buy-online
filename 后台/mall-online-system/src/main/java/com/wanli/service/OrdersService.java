package com.wanli.service;

import com.wanli.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wanli.entity.dto.BusinessDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
public interface OrdersService extends IService<Orders> {
    public List<BusinessDto> getOrdersCount(String timeRange);
    public List<BusinessDto> getBusinessSalesRank(String timeRange);

}
