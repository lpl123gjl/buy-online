package com.wanli.security;

import cn.hutool.json.JSONUtil;
import com.wanli.common.Result;
import com.wanli.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = httpServletResponse.getOutputStream();

        String username = authentication.getName();
        String token = jwtUtil.createToken(username);
        httpServletResponse.setHeader(jwtUtil.getHeader(),token);
        log.info("token==={}",token);

        Result success = Result.success("验证成功");

        out.write(JSONUtil.toJsonStr(success).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
