package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.entity.Business;
import com.soft.entity.Cart;
import com.soft.entity.Goods;
import com.soft.entity.dto.BusinessWithGoodsDto;
import com.soft.mapper.CartMapper;
import com.soft.service.BusinessService;
import com.soft.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Byterain
 * @since 2024-01-12
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private GoodsService goodsService;

    public List<BusinessWithGoodsDto> getBusinessesWithGoodsByAccountId(String accountId) {
        List<Cart> cartList = cartMapper.selectList(new QueryWrapper<Cart>().eq("account_id",accountId));

        Map<Long, List<Goods>> businessGoodsMap = new HashMap<>();
        for (Cart cart : cartList) {
            Long businessId = cart.getBusinessId();
            Goods goods = goodsService.getById(cart.getGoodsId());
            goods.setCount(cart.getQuantity());
            if (!businessGoodsMap.containsKey(businessId)) {
                businessGoodsMap.put(businessId, new ArrayList<>());
            }
            businessGoodsMap.get(businessId).add(goods);
        }

        List<BusinessWithGoodsDto> businessWithGoodsList = new ArrayList<>();
        for (Long businessId : businessGoodsMap.keySet()) {
            Business business = businessService.getById(businessId);
            BusinessWithGoodsDto businessWithGoodsDto = new BusinessWithGoodsDto();
            businessWithGoodsDto.setBusinessId(businessId);
            businessWithGoodsDto.setBusinessName(business.getBusinessName());
            businessWithGoodsDto.setGoodsList(businessGoodsMap.get(businessId));
            businessWithGoodsList.add(businessWithGoodsDto);
        }

        return businessWithGoodsList;
    }
}
