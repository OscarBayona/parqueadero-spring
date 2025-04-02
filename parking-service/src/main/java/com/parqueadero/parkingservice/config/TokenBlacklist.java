package com.parqueadero.parkingservice.config;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBlacklist {

    private static final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    public static void addToken(String jti, long expirationEpochSeconds) {
        blacklist.put(jti, expirationEpochSeconds);
    }

    public static boolean isTokenBlacklisted(String jti) {
        Long exp = blacklist.get(jti);
        if (exp == null) {
            return false;
        }
        // Si ya expiró, se remueve de la blacklist
        if (exp < Instant.now().getEpochSecond()) {
            blacklist.remove(jti);
            return false;
        }
        return true;
    }
}