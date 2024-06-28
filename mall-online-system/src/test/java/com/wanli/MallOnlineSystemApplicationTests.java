package com.wanli;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanli.entity.User;
import com.wanli.mapper.UserMapper;
import com.wanli.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MallOnlineSystemApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        userMapper.selectList(null);
         QueryWrapper<User> queryWrapper = new QueryWrapper<>();
         queryWrapper.eq("username","suzuki");
         userMapper.selectOne(queryWrapper);

    }
    @Test
    void password(){
        User user = new User();
        String password = "123456";
        String encode = bCryptPasswordEncoder.encode(password);
        user.setUsername("admin");
        user.setPassword(encode);
        user.setStatu(1);
        userMapper.insert(user);
    }
    @Test
    void AuthorityString(){
        userService.getUserAuthorityInfo(1l);
    }

}
