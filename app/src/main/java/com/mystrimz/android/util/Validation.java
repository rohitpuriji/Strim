package com.mystrimz.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manishjoshi on 3/1/18.
 */

public class Validation {
    private static final Validation ourInstance = new Validation();

    public static Validation getInstance() {
        return ourInstance;
    }

    private Validation() {
    }


    public boolean isEmpty(String name){
        if (name.length() != 0){
            return true;
        }else {
            return false;
        }
    }
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }
}
