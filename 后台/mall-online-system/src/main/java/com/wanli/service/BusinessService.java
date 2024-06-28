package com.wanli.service;

import com.wanli.entity.Business;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzy
 * @since 2024-06-21
 */
public interface BusinessService extends IService<Business> {
    public List<Long> getDifferentBusinessIdList();
}
