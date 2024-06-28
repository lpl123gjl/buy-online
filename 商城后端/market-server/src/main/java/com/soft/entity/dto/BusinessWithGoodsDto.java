package com.soft.entity.dto;

import com.soft.entity.Goods;
import lombok.Data;

import java.util.List;

@Data
public class BusinessWithGoodsDto {
    private Long businessId;
    private String businessName;
    private List<Goods> goodsList;
}
