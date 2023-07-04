package com.aacdemy.moonlight.util;

import static com.aacdemy.moonlight.util.RandomString.generateRandomString;

public class RandomEmail {

    public static String generateRandomEmail() {
        return generateRandomString(6) + "@gmail.com";
    }
}