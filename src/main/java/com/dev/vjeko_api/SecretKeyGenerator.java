package com.dev.vjeko_api;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // Generate a new key using the HS256 algorithm
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Encode the key as a Base64 string
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("New JWT Secret (Base64-encoded): " + base64Secret);
    }
}