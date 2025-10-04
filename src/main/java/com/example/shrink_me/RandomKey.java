package com.example.shrink_me;

import java.util.Random;

public class RandomKey {
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int KEY_LENGTH = 7;
    private static final int BASE = BASE62.length();
    private static final Random random = new Random();

    public static String generate() {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++) {
            key.append(BASE62.charAt(random.nextInt(BASE)));
        }
        return key.toString();
    }
}
