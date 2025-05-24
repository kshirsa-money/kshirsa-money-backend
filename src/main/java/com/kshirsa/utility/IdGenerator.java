package com.kshirsa.utility;

import java.security.SecureRandom;
import java.util.UUID;

public class IdGenerator {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final SecureRandom RANDOM = new SecureRandom();

    public static String generateUserId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return ("U" + sb);
    }

    public static String generateTransactionId() {
        return "T" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateCategoryId() {
        return "C" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateLoanId() {
        return "L" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
