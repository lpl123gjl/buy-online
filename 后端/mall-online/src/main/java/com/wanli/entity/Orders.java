package com.wanli.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wanli.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
@Data
@TableName("sys_orders")
public class Orders{

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 下单用户编号--sys_account表account_id	
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 商家编号--sys_business表business_id
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 订单总价
     */
    @TableField("order_total")
    private BigDecimal orderTotal;

    /**
     * 送货地址编号--sys_deliveryaddress表da_id
     */
    @TableField("da_id")
    private Integer daId;

    /**
     * 订单状态（0：未支付； 1：已支付）
     */
    @TableField("state")
    private Integer state;

    @TableField("created")
    private LocalDateTime created;

    @TableField("updated")
    private LocalDateTime updated;

    @TableField(exist = false)
    private Business business;

    @TableField(exist = false)
    private Deliveryaddress deliveryaddress;

    @TableField(exist = false)
    private List<Ordersdetailet> ordersDetailets;

    @TableField(exist = false)
    private Integer ordersCount;
    @TableField(exist = false)
    private BigDecimal sales;
}
