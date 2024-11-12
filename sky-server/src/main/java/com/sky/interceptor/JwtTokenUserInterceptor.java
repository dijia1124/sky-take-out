package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * interceptor for jwt token user
 */

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * verify jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // determine whether the current interception is a method of the Controller or other resources
        if (!(handler instanceof HandlerMethod)) {
            // if the current interception is not a dynamic method, release it directly
            return true;
        }

        // 1. get token from request header
        String token = request.getHeader(jwtProperties.getUserTokenName());

        // 2. verify token
        try {
            log.info("jwt verification:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("current user id: {}", userId);
            // put current user id into thread local
            BaseContext.setCurrentId(userId);
            // 3. pass
            return true;
        } catch (Exception ex) {
            // 4. fail, response 401 status code
            response.setStatus(401);
            return false;
        }
    }
}
