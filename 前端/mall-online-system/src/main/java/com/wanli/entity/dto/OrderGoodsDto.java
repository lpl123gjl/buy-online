package com.wanli.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderGoodsDto {

    private Integer goodsId;
    private String goodsName;
    private Integer quantity;
    private String sales;
}
