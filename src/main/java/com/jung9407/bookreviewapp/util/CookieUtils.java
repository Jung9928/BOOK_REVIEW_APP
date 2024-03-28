package com.jung9407.bookreviewapp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${jwt.live.rtk}")
    private int refreshToken;

    @Value("${jwt.live.atk}")
    private int accessToken;

    public Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");

        if(cookieName.equals("Access")) {
            cookie.setMaxAge(accessToken);
        } else if(cookieName.equals("Refresh")) {
            cookie.setMaxAge(refreshToken);
        }

        cookie.setPath("/");

        return cookie;
    }


    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public Cookie deleteCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        cookie.setMaxAge(0);

        cookie.setPath("/");

        return cookie;
    }
}
