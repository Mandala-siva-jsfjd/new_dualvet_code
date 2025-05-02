//package com.strive.auth;
//
//
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@Component
//public class CustomHeaderFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        // Add COOP and COEP headers
//        httpResponse.setHeader("Cross-Origin-Opener-Policy", "same-origin-allow-popups");
//        httpResponse.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");
//
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // No initialization needed
//    }
//
//    @Override
//    public void destroy() {
//        // No cleanup needed
//    }
//}
