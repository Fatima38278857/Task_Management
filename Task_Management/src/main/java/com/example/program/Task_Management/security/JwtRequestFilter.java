package com.example.program.Task_Management.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestUri = request.getRequestURI();

        // Исключение Swagger URL из фильтрации
        if (requestUri.startsWith("/swagger-ui") ||
                requestUri.startsWith("/v3/api-docs") ||
                requestUri.startsWith("/register") ||
                requestUri.startsWith("/create") ||
                requestUri.startsWith("/swagger-resources") ||
                requestUri.startsWith("/webjars")) {
            chain.doFilter(request, response);
            return;
        }
        final String authorizationHeader = request.getHeader("Authorization");
        logger.info("Запрос обработан в JwtRequestFilter");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);

            try {
                String username = jwtUtil.extractUsername(jwtToken);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("Аутентификация прошла успешно для пользователя: {}", username);
                    }
                }

            } catch (MalformedJwtException exception) {
                logger.error("JWT имеет неправильный формат.", exception);
                handleErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Неверный токен");
                return;
            } catch (ExpiredJwtException exception) {
                logger.error("JWT истек.", exception);
                handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Токен истек");
                return;
            } catch (Exception exception) {
                logger.error("Ошибка при обработке JWT.", exception);
                handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void handleErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
