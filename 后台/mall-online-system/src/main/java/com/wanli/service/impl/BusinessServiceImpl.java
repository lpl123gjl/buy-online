package com.wanli.service.impl;

import com.wanli.entity.Business;
import com.wanli.mapper.BusinessMapper;
import com.wanli.mapper.UserBusinessMapper;
import com.wanli.service.BusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements BusinessService {
    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private UserBusinessMapper userBusinessMapper;

    public List<Long> getDifferentBusinessIdList() {
        // 查询business表中的所有business_id列表
        List<Long> allBusinessIds = businessMapper.selectList(null).stream()
                .map(business -> business.getBusinessId())
                .collect(Collectors.toList());

        // 查询user_business表中的所有business_id列表
        List<Long> userBusinessIds = userBusinessMapper.selectList(null).stream()
                .map(userBusiness -> userBusiness.getBusinessId())
                .collect(Collectors.toList());

        // 找出在business表中存在但在user_business表中不存在的business_id
        List<Long> differentBusinessIdList = allBusinessIds.stream()
                .filter(businessId -> !userBusinessIds.contains(businessId))
                .collect(Collectors.toList());

        return differentBusinessIdList;
    }
}
