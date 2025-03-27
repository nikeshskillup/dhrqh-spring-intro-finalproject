package com.app.quiz.service;

import com.app.quiz.model.User; // Keep this import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// Remove import for org.springframework.security.core.userdetails.User;
// import com.app.quiz.model.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
public class QuizUserDetailsService implements UserDetailsService {
    private final Map<String, User> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Load user by username and validate credentials
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // Register a new user
    public boolean registerUser(String username, String email, String password, String role) {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }

        String encodedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, email, encodedPassword, role);
        users.put(username, newUser);
        return true;
    }

    // Helper method to get the password encoder (for testing purposes)
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
