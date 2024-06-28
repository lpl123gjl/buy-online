package com.wanli.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wanli.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
@RestController
public class BaseController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected RedisUtil redisUtil;
    public Page getPage(){
        try {
            Integer current = ServletRequestUtils.getIntParameter(request,"current");
            Integer size = ServletRequestUtils.getIntParameter(request,"size");
            return new Page(current, size);
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
