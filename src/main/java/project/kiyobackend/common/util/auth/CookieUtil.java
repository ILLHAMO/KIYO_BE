package project.kiyobackend.common.util.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
        }
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(100000000);
        response.addCookie(cookie);
//                ResponseCookie cookie = ResponseCookie.from(name, value)
//                .path("/")
//                .httpOnly(true)
////                .sameSite("None")
////                .secure(true)
//                .domain("jmsteady.net")
//                .maxAge(100000000L)
//                .build();
        response.addHeader("Set-Cookie",cookie.toString());
    }

//    public static void addCookieForLogin(HttpServletResponse response, String name, String value, int maxAge) {
//        ResponseCookie cookie = ResponseCookie.from(name, value)
//                .path("/")
//                .httpOnly(true)
//                .sameSite("None")
//                .secure(true)
//                .maxAge(100000000L)
//                .build();
//        response.addHeader("Set-Cookie",cookie.toString());
//
//    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        System.out.println(cookies);
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
