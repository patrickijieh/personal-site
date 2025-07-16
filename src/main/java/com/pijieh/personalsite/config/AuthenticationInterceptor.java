package com.pijieh.personalsite.config;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Middleware class that checks if a user's session is valid.
 *
 * @author patrickijieh
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (null == session || null == session.getAttribute("username")
                || session.getAttribute("username").toString().isEmpty()) {

            String address = request.getHeader("X-Real-IP");
            if (address == null || address.isEmpty()) {
                address = request.getRemoteAddr();
            }
            logger.warn("UNAUTHORIZED request to /admin intercepted from addr {}", address);
            response.sendError(403, "Forbidden");
            return false;
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, @Nullable Exception ex) throws Exception {

    }
}
