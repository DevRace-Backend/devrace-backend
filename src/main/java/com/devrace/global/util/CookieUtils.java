package com.devrace.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.springframework.util.SerializationUtils;

public abstract class CookieUtils {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_COOKIE_DURATION = Duration.ofDays(14);

    private CookieUtils() { }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .findAny();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = createToken(name, value, maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void addRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = createRefreshTokenCookie(refreshToken);
        response.addCookie(cookie);
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    private static Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = createToken(REFRESH_TOKEN_COOKIE_NAME, refreshToken, Math.toIntExact(REFRESH_TOKEN_COOKIE_DURATION.toSeconds()));
        return cookie;
    }

    private static Cookie createToken(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
