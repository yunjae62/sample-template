package ex.sample.global.security.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlConstant {

    public static final String SIGNUP_URL = "/users/signup";
    public static final String LOGIN_URL = "/users/login";
    public static final String LOGOUT_URL = "/users/logout";
    public static final String REFRESH_URL = "/users/refresh";

}