package com.kjbg.fliter;

import com.alibaba.fastjson.JSON;
import com.kgbg.cons.SecuretiyCons;
import com.kgbg.dto.ResponseResult;
import com.kgbg.util.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //设置网关头部的自定义验证信息
//        ctx.getResponse().setHeader(SecuretiyCons.AUTH_NAME,SecuretiyCons.AUTH_SECRET);
        //TODO 校验过滤地址
        String requestURI = ctx.getRequest().getRequestURI();
        Pattern pattern = Pattern.compile("/system/login/*.*",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(requestURI);
        boolean matches = matcher.matches();
        if(matches)return false;
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String auth = request.getHeader("Authorization");
        if(auth==null||"".equals(auth)){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            ctx.getResponse().setContentType("application/json; charset=utf-8");
            ResponseResult responseResult = new ResponseResult(HttpStatus.SC_UNAUTHORIZED, "没有令牌");
            ctx.setResponseBody(JSON.toJSONString(responseResult));
        }else{
            int index = auth.indexOf(SecuretiyCons.BEARER_NAME);
            if(index!=-1){
                auth = auth.substring(index+(SecuretiyCons.BEARER_NAME+" ").length());
            }
            boolean b = JwtUtils.parseJwt(auth);
            if(!b){
                ctx.setSendZuulResponse(false);
                ctx.getResponse().setContentType("application/json; charset=utf-8");
                ResponseResult responseResult = new ResponseResult(HttpStatus.SC_UNAUTHORIZED, "令牌失效");
                ctx.setResponseBody(JSON.toJSONString(responseResult));
            }
        }
        return null;
    }
}
