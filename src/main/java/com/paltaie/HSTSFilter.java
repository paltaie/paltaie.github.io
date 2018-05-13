package com.paltaie;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class HSTSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Optional<String> headerOptional = Collections.list(((HttpServletRequest) servletRequest).getHeaderNames())
                .stream()
                .filter(header -> header.toUpperCase().equals("X-FORWARDED-PROTO"))
                .findFirst();
        if (headerOptional.isPresent()) {
            String header = headerOptional.get();
            if (httpServletRequest.getHeader(header).toUpperCase().equals("HTTP")) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse
                        .sendRedirect("https://" + servletRequest.getServerName() + httpServletRequest.getServletPath());
                return;
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
