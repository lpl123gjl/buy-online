package com.wanli.security;

import cn.hutool.core.util.StrUtil;
import com.wanli.entity.User;
import com.wanli.service.UserService;
import com.wanli.service.impl.UserDetailsServiceImpl;
import com.wanli.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeSet;

@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader("token");
        log.info("Jwt校验过滤器执行，Jwt={}",jwt);

        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }
        Claims claims = jwtUtil.getClaimsToken(jwt);
        if(claims==null){
            throw new JwtException("Token解析异常");
        }
        if(jwtUtil.isTokenExpired(claims)){
            throw new JwtException("Token已经过期");
        }

        String username = claims.getSubject();
        log.info("Jwt验证成功，用户{}---正在访问后台系统",username);

        User user = userService.getUserByUsername(username);

        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username,null,userDetailsService.getUserAuthority(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(upat);

        chain.doFilter(request,response);
    }
}
