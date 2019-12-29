package com.kjbg.filter;

import com.alibaba.fastjson.JSON;
import com.kgbg.cons.SecuretiyCons;
import com.kgbg.dto.ResponseResult;
import org.apache.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class SecurityFilter implements Filter {

    private Set<String> gatewayAddress;

    public SecurityFilter(Set<String> gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
//        String headerAuth = httpServletRequest.getHeader(SecuretiyCons.AUTH_NAME);
        String remoteHost = httpServletRequest.getRemoteHost();
        if(gatewayAddress.contains(remoteHost)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            httpServletResponse.setContentType("application/json; charset=utf-8");
            httpServletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
            PrintWriter writer = httpServletResponse.getWriter();
            ResponseResult responseResult = new ResponseResult(HttpStatus.SC_UNAUTHORIZED, "不允许的请求");
            writer.write(JSON.toJSONString(responseResult));
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void destroy() {

    }
}
