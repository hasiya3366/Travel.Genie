package com.example.TravelApp.service;

import com.example.TravelApp.model.User;
import com.example.TravelApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setResetToken(null);   // ටෝකන් එක පාවිච්චි කළ නිසා මකලා දානවා
        user.setTokenExpiry(null);  // Expiry කාලයත් අයින් කරනවා
        userRepository.save(user);  // අලුත් පස්වර්ඩ් එක ඩේටාබේස් එකේ සේව් වේ
    }
}