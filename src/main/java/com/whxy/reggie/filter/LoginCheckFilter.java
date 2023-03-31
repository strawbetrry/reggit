package com.whxy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.whxy.reggie.common.BaseContext;
import com.whxy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("本次获取的请求路径--->{}",requestURI);
        //以下路径不需要处理
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //2、检查本次请求是否需要处理，true表示不需要处理
        boolean check = check(urls, requestURI);
        //3、如果不需要处理，直接放行
        if(check){
            log.info("本次请求路径不需要处理--->{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4、判断登录状态，如果已经登录则放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("登录成功--->{}",request.getSession().getAttribute("employee"));
            BaseContext.set((Long)request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录，通过输出流方式向页面响应数据
        //前端写了响应拦截器，这里必须返回一个json数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    public boolean check(String[] urls,String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
