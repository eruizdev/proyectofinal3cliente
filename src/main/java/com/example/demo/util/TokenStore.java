package com.example.demo.util;

/**
 * Almacena el JWT en memoria para usarlo en todas las peticiones.
 */
public class TokenStore {
    private static String token;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }
}