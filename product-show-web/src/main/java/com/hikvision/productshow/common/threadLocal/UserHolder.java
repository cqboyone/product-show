package com.hikvision.productshow.common.threadLocal;


import com.hikvision.productshow.module.entity.TUser;

public class UserHolder {

    private static final String ADMIN = "admin";

    private static ThreadLocal<TUser> userHolder = new ThreadLocal<>();

    public static void holdUser(TUser user) {
        userHolder.set(user);
    }

    public static TUser getUser() {
        return userHolder.get();
    }

    public static String getUserName() {
        return userHolder.get() != null ? userHolder.get().getUserName() : null;
    }

    public static String getUserId() {
        return userHolder.get() != null ? userHolder.get().getId() : null;
    }

    public static void clearUser() {
        userHolder.remove();
    }

    public static boolean isAdmin() {
        return userHolder.get() != null && ADMIN.equals(userHolder.get().getUserName());
    }


}
