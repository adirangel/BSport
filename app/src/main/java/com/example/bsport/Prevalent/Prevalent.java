package com.example.bsport.Prevalent;

import com.example.bsport.Model.Users;

public class Prevalent {
    public static String UserNameKey = "UserName";
    public static String UserPasswordKey = "UserPassword";
    public static String UserAdminKey = "false";

    public Prevalent(String UserNameKey, String UserPasswordKey,String UserAdminKey) {
        this.UserNameKey = UserNameKey;
        this.UserPasswordKey = UserPasswordKey;
        this.UserAdminKey = UserAdminKey;

    }

    public static String getUserNameKey() {
        return UserNameKey;
    }

    public static void setUserNameKey(String userNameKey) {
        UserNameKey = userNameKey;
    }

    public static String getUserPasswordKey() {
        return UserPasswordKey;
    }

    public static void setUserPasswordKey(String userPasswordKey) {
        UserPasswordKey = userPasswordKey;
    }

    public static String getUserAdminKey() {
        return UserAdminKey;
    }

    public static void setUserAdminKey(String userAdminKey) {
        UserAdminKey = userAdminKey;
    }
}
