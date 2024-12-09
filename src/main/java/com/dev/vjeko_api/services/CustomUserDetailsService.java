package com.dev.vjeko_api.services;

import com.dev.vjeko_api.entities.User;
import com.dev.vjeko_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.dev.vjeko_api.custom.CustomUserDetails;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> credential = userRepository.findUserByUsername(username);
        return credential.map(
                CustomUserDetails::new
        ).orElseThrow(() -> new UsernameNotFoundException("Username not found in system" + username));
    }
}
