package com.example.bsport;

import android.app.Instrumentation;
import android.widget.Button;
import static org.junit.Assert.*;


import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

public class LoginActivityTest {

    @Test
    public void userNameNotEmpty_oneTest_ReturnTrue(){
        assertTrue(LoginActivity.Checking_username_notEmpty("userName"));
    }
    @Test
    public void userNameNotEmpty_twoTest_ReturnTrue(){
        assertTrue(LoginActivity.Checking_username_notEmpty("f1"));
    }
    @Test
    public void userNameNotEmpty_oneTest_ReturnFalse(){
        assertFalse(LoginActivity.Checking_username_notEmpty(""));
    }
    @Test
    public void userNameValidator_oneTest_ReturnTrue(){
        assertTrue(LoginActivity.Valid_username_check("userName123"));
    }
    @Test
    public void userNameValidator_twoTest_ReturnTrue(){
        assertTrue(LoginActivity.Valid_username_check("123userName"));
    }
    @Test
    public void userNameValidator_oneTest_ReturnFalse(){
        assertFalse(LoginActivity.Valid_username_check("user."));
    }
    @Test
    public void userNameValidator_twoTest_ReturnFalse(){
        assertFalse(LoginActivity.Valid_username_check("tr*/fds"));
    }
    @Test
    public void passwordNotEmpty_oneTest_ReturnTrue(){
        assertTrue(LoginActivity.Checking_password_notEmpty("f1"));
    }
    @Test
    public void passwordNotEmpty_twoTest_ReturnTrue(){
        assertTrue(LoginActivity.Checking_password_notEmpty("123456"));
    }
    @Test
    public void passwordNotEmpty_oneTest_ReturnFalse(){
        assertFalse(LoginActivity.Checking_password_notEmpty(""));
    }
    @Test
    public void passwordValidator_oneTest_ReturnTrue(){
        assertTrue(LoginActivity.Valid_password_check("1234as"));
    }
    @Test
    public void passwordValidator_oneTest_ReturnFalse(){
        assertFalse(LoginActivity.Valid_password_check("12345"));
    }
/*
    @Test
    public void validTestemail() {
        assertEquals(LoginActivity.UserName,null);
    }
    @Test
    public void ValidTestpass(){
        assertEquals(LoginActivity.UserPassword,null);
    }*/
}