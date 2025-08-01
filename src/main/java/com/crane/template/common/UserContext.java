package com.crane.template.common;


/**
 * @author crane
 * @date 2024.10.11 下午1:25
 * @description
 **/
public class UserContext {

    private static final ThreadLocal<TokenUserInfo> T = new ThreadLocal<>();

    public static void setUser(TokenUserInfo user) {
        T.set(user);
    }

    public static TokenUserInfo getUser() {
        return T.get();
    }

    public static void removeUser() {
        T.remove();
    }
}
