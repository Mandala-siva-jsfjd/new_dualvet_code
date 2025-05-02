package com.strive.auth;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SetCrossOriginHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Set the Cross-Origin-Opener-Policy header to allow same-origin access or other necessary settings
        response.setHeader("Cross-Origin-Opener-Policy", "same-origin");  // Or use another appropriate value

        // You can also consider setting Cross-Origin-Embedder-Policy depending on your needs
        response.setHeader("Cross-Origin-Embedder-Policy", "require-corp");

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
