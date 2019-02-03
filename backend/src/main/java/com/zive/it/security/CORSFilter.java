package com.zive.it.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CORSFilter extends OncePerRequestFilter {
//
////    public CORSFilter () {
////        super();
////    }
////
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////        response.setHeader("Access-Control-Allow-Origin", "*");
////        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
////        response.setHeader("Access-Control-Max-Age", "3600");
////        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
////        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
////        if ("OPTIONS".equals(request.getMethod())) {
////            response.setStatus(HttpServletResponse.SC_OK);
////        } else {
////            filterChain.doFilter(request, response);
////        }
////    }
////
////    @Override
////    public void destroy() {
////
////    }
//
//}