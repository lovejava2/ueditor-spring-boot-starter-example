package com.example.ueditor.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: chaolong
 * Date: 2017/9/20
 * Time: 15:07
 * CopyRight @ 2016 IIOT
 */
public class PreHandlerInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(PreHandlerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle:" + request.getRequestURI());
//        request.setAttribute("API_URL", ConfigUtil.getOutApiUrl());
        //TODO 部署时需要修改
//        request.setAttribute("API_URL", "http://106.75.66.7:8080");
        request.setAttribute("API_URL", "http://127.0.0.1:10060");
//        request.setAttribute("STATIC_URL", ConfigUtil.getStaticUrl());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
