package com.wanli.security;

import cn.hutool.json.JSONUtil;
import com.wanli.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        ServletOutputStream out = httpServletResponse.getOutputStream();
        log.info("用户权限不足--");

        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Result fail = Result.fail("权限不足" + e.getMessage());

        out.write(JSONUtil.toJsonStr(fail).getBytes("utf-8"));
        out.flush();
        out.close();

    }
}
