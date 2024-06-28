package com.wanli.security;

import cn.hutool.json.JSONUtil;
import com.wanli.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = httpServletResponse.getOutputStream();

        Result fail = Result.fail(e.getMessage().equals("Bad credentials")?"用户名或密码错误":e.getMessage());

        out.write(JSONUtil.toJsonStr(fail).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
