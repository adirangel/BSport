package com.example.bsport;

import android.app.Instrumentation;
import android.widget.Button;

import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

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