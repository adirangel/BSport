package com.example.bsport;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterActivityTest {

    @Test
    public void validTestemail() {
        assertEquals(RegisterActivity.UserEmail, null);
    }

    @Test
    public void ValidTestpass() {
        assertEquals(RegisterActivity.UserPassword, null);
    }
    @Test
    public void ValidTestage() {
        assertEquals(RegisterActivity.UserAge, null);
    }
    @Test
    public void ValidTestpMyself() {
        assertEquals(RegisterActivity.UserAboutMyself, null);
    }
    @Test
    public void ValidTestName() {
        assertEquals(RegisterActivity.UserName, null);
    }

}