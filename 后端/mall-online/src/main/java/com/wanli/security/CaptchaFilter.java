package com.wanli.security;

import com.wanli.config.Const;
import com.wanli.exception.CaptchaException;
import com.wanli.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
    private final String loginURL = "/login";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String uri = httpServletRequest.getRequestURI();
        if(uri.equals(loginURL)&&httpServletRequest.getMethod().equals("POST")){
            log.info("获得login请求连接，正在校验验证码,请求路径---{}",uri);
            try {
                validate(httpServletRequest);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest request) throws CaptchaException {
//登录操作，提交请求参数：
        String code = request.getParameter("code"); //前端输入的验证码
        String key = request.getParameter("key"); //存储到redis验证码的键
        if(StringUtils.isBlank(code)){
//验证码是null，验证失败。抛出自定义异常。
            throw new CaptchaException("验证码不能为空");

        }
        log.info(String.valueOf(redisUtil.hasKey(key)));
        if(!redisUtil.hHasKey(Const.CAPTCHA,key)){
            throw new CaptchaException("验证码已刷新");
        }
        if(!redisUtil.hget(Const.CAPTCHA,key).equals(code)){
            throw new CaptchaException("验证码不正确");
        }
//验证成功: 验证成功，需要将验证码从redis中删除（一次性）。

        redisUtil.hdel(Const.CAPTCHA,key);
    }

}
