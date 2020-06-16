package com.example.bsport;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final int MILLIS = 1000;

    public static boolean checkEmailForValidity(String email) {


        email = email.trim();

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();

    }
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                                    +
                                    "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                                    +
                                    "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                                    +
                                    ")+", Pattern.CASE_INSENSITIVE);


    public static Date calendarDate(long epocSeconds) {
        Calendar c = Calendar.
                getInstance(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(epocSeconds * MILLIS);
        return c.getTime();
    }

    public static boolean checkPasswordLength(final String Password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "[0-9a-zA-Z]{6,}";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(Password);

        return matcher.matches();
    }

}

