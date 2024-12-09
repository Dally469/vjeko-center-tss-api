package com.dev.vjeko_api;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Key;
import java.util.Base64;

@SpringBootApplication
public class VjekoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VjekoApiApplication.class, args);

	}

}
