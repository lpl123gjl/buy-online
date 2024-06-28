package com.wanli.mapper;

import com.wanli.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanli.entity.dto.OrderGoodsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    List<OrderGoodsDto> getOrderGoodsSummaryByBusinessId(Long businessId);
}
