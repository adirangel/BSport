package com.example.bsport.Prevalent;

import com.example.bsport.Model.Users;

public class Prevalent {
    public static String UserNameKey = "UserName";
    public static String UserPasswordKey = "UserPassword";
    public static String UserAdminKey = "false";
    public static String UserName;
    public static String UserPassword;

    public Prevalent(String UserNameKey, String UserPasswordKey,String UserAdminKey,String UserName,String UserPassword) {
        Prevalent.UserNameKey = UserNameKey;
        Prevalent.UserPasswordKey = UserPasswordKey;
        Prevalent.UserAdminKey = UserAdminKey;
        Prevalent.UserName = UserName;
        Prevalent.UserPassword = UserPassword;

    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getUserPassword() {
        return UserPassword;
    }

    public static void setUserPassword(String userPassword) {
        UserPassword = userPassword;
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
