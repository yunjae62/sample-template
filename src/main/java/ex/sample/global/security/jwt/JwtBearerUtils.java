package ex.sample.global.security.jwt;

import org.springframework.util.StringUtils;

public class JwtBearerUtils {

    public static final String BEARER_PREFIX = "Bearer ";

    public static boolean hasBearerPrefix(String token) {
        return StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX);
    }

    public static String removePrefix(String token) {
        return hasBearerPrefix(token) ? token.substring(BEARER_PREFIX.length()) : token;
    }

    public static String addPrefix(String token) {
        return hasBearerPrefix(token) ? token : BEARER_PREFIX + token;
    }
}