package com.example.finalproyectmovil.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MarvelUtils {
    public static final String MARVEL_API_BASE_URL = "https://gateway.marvel.com/v1/public/";
    public static final String PUBLIC_KEY = "98eb4551418b5d5a75180872a5b87947";
    public static final String PRIVATE_KEY = "b938e1bfcc06e3a09003f19d0ed88495da6f4a0b";
    public static final String TS = "1";

    public static String generateHash() {
        String input = TS + PRIVATE_KEY + PUBLIC_KEY;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
} 