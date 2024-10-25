package com.devrace.global.config.security.filter;

import com.devrace.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class GlobalExceptionFilterHandler extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ErrorCode.EXPIRED_TOKEN);
        } catch (AuthenticationException | IllegalArgumentException e) {
            setErrorResponse(HttpStatus.FORBIDDEN, response, ErrorCode.UNAUTHORIZED);
        } catch (JwtException e) {
            setErrorResponse(HttpStatus.NOT_FOUND, response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        ResponseEntity<String> fail = ResponseEntity.status(errorCode.getHttpStatus()).body(errorCode.getMessage());
        try {
            String result = mapper.writeValueAsString(fail);
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}