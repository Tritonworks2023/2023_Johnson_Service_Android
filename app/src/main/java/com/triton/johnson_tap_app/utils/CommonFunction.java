package com.triton.johnson_tap_app.utils;

public class CommonFunction {

    public static String nullPointer(String value) {
        if (value != null && !value.equalsIgnoreCase("null")
                && !value.equalsIgnoreCase("") && !value.isEmpty()) {
            return value;
        } else {
            return "";
        }
    }

    public static Boolean nullPointerValidator(String value) {
        if (value != null && !value.equalsIgnoreCase("null")
                && !value.equalsIgnoreCase("") && !value.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
