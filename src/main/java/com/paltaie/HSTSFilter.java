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

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse resp = (HttpServletResponse) res;

        Optional<String> headerOptional = Collections.list(((HttpServletRequest) req).getHeaderNames())
                .stream()
                .filter(header -> header.toUpperCase().equals("X-FORWARDED-PROTO"))
                .findFirst();
        if (headerOptional.isPresent()) {
            String header = headerOptional.get();
            if (httpServletRequest.getHeader(header).toUpperCase().equals("HTTP")) {
                resp.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains");
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
