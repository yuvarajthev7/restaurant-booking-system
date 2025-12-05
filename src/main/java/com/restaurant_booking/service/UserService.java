package com.restaurant_booking.service;

import com.restaurant_booking.model.User;
import com.restaurant_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        // --- CHECK: Unique Government ID ---
        if (userRepository.findByGovernmentId(user.getGovernmentId()).isPresent()) {
            throw new RuntimeException("Error: Government ID already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
