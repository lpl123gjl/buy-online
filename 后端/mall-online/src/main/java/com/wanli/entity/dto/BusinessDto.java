package com.wanli.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BusinessDto {
    private Long businessId;
    private String businessName;
    private String businessExplain;
    private LocalDateTime created;

    private Integer ordersCount;
    private String sales;


}
