package com.wanli.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.UserBusiness;
import com.wanli.mapper.BusinessMapper;
import com.wanli.mapper.UserBusinessMapper;
import com.wanli.service.UserBusinessService;
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
 * @since 2024-06-23
 */
@Service
public class UserBusinessServiceImpl extends ServiceImpl<UserBusinessMapper, UserBusiness> implements UserBusinessService {

}
