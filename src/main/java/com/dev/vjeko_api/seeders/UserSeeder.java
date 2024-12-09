package com.dev.vjeko_api.seeders;

import com.dev.vjeko_api.entities.User;
import com.dev.vjeko_api.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Using BCryptPasswordEncoder, which is a commonly used encoder
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the users already exist, so we don't duplicate on each run
        if (userRepository.count() == 0) {
            seedUsers();
        }
    }

    private void seedUsers() {
        User user1 = new User(
                null, // UUID will be generated automatically
                "admin@vjekorwanda.org.rw",
                passwordEncoder.encode("password123"), // Encode password before saving
                "System",
                "Administrator",
                "admin@vjekorwanda.org.rw",
                "+1234567890",
                "ADMIN"
        );

        User user2 = new User(
                null, // UUID will be generated automatically
                "robert@vjekorwanda.org.rw",
                passwordEncoder.encode("password456"), // Encode password before saving
                "Robert",
                "IRADUKUNDA",
                "robert@vjekorwanda.org.rw",
                "+1987654321",
                "ADMIN"
        );

        // Save users to the database
        userRepository.saveAll(Arrays.asList(user1, user2));
        System.out.println("Users seeded successfully.");
    }
}
