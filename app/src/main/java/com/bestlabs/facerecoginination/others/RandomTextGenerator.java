package com.bestlabs.facerecoginination.others;

import java.util.Random;

public class RandomTextGenerator {
    public static String getRandomNumber(int digits) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(digits);
        for (int i = 0; i < digits; i++) {
            sb.append(random.nextInt(10)); // Generates a digit between 0 and 9
        }
        return sb.toString();
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}

