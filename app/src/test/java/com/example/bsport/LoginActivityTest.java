package com.example.bsport;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void validTestemail() {
        assertEquals(LoginActivity.UserName,null);
    }
    @Test
    public void ValidTestpass(){
        assertEquals(LoginActivity.UserPassword,null);
    }
}