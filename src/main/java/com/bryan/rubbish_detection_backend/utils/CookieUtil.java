package com.bryan.rubbish_detection_backend.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookieUtil {

    private CookieUtil() {
    }


    public static boolean addCookie(final Cookie cookie, final HttpServletResponse response) {
        if (cookie == null || response == null) {
            return false;
        }

        response.addCookie(cookie);
        return true;
    }

    public static @Nullable Cookie getCookie(final HttpServletRequest request, final String cookieName) {
        if (StringUtils.isBlank(cookieName)) {
            return null;
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (final Cookie cookie : cookies) {
            if (StringUtils.equals(cookieName, cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    public static @NotNull List<Cookie> getCookies(final HttpServletRequest request, final String regex) {
        final ArrayList<Cookie> foundCookies = new ArrayList<Cookie>();
        if (StringUtils.isBlank(regex)) {
            return foundCookies;
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Collections.emptyList();
        }

        final Pattern p = Pattern.compile(regex);
        for (final Cookie cookie : cookies) {
            final Matcher m = p.matcher(cookie.getName());
            if (m.matches()) {
                foundCookies.add(cookie);
            }
        }

        return foundCookies;
    }

    public static boolean extendCookieLife(final HttpServletRequest request, final HttpServletResponse response,
                                           final String cookieName, final String cookiePath, final int expiry) {
        final Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return false;
        }

        if (cookie.getMaxAge() <= 0) {
            return false;
        }

        final Cookie responseCookie = (Cookie) cookie.clone();
        responseCookie.setMaxAge(expiry);
        responseCookie.setPath(cookiePath);

        addCookie(responseCookie, response);

        return true;
    }


    public static int dropCookies(final HttpServletRequest request, final HttpServletResponse response, final String cookiePath, final String... cookieNames) {
        int count = 0;
        if (cookieNames == null) {
            return count;
        }

        final List<Cookie> cookies = new ArrayList<Cookie>();
        for (final String cookieName : cookieNames) {
            cookies.add(getCookie(request, cookieName));
        }

        return dropCookies(response, cookies.toArray(new Cookie[cookies.size()]), cookiePath);
    }


    private static int dropCookies(final HttpServletResponse response, final Cookie @NotNull [] cookies, final String cookiePath) {
        int count = 0;

        for (final Cookie cookie : cookies) {
            if (cookie == null) {
                continue;
            }

            final Cookie responseCookie = (Cookie) cookie.clone();
            responseCookie.setMaxAge(0);
            responseCookie.setPath(cookiePath);
            responseCookie.setValue("");

            addCookie(responseCookie, response);
            count++;
        }

        return count;
    }


    public static int dropCookiesByRegex(final HttpServletRequest request, final HttpServletResponse response, final String cookiePath, final String... regexes) {
        return dropCookiesByRegexArray(request, response, cookiePath, regexes);
    }


    public static int dropCookiesByRegexArray(final HttpServletRequest request, final HttpServletResponse response, final String cookiePath, final String[] regexes) {
        int count = 0;
        if (regexes == null) {
            return count;
        }
        final List<Cookie> cookies = new ArrayList<Cookie>();

        for (final String regex : regexes) {
            cookies.addAll(getCookies(request, regex));
        }

        return dropCookies(response, cookies.toArray(new Cookie[cookies.size()]), cookiePath);
    }


    public static int dropAllCookies(final @NotNull HttpServletRequest request, final HttpServletResponse response, final String cookiePath) {
        final Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return 0;
        }

        return dropCookies(response, cookies, cookiePath);
    }
}
