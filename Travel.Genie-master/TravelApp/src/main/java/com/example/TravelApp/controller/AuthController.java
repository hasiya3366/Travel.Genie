package com.example.TravelApp.controller;

import com.example.TravelApp.model.User;
import com.example.TravelApp.service.EmailService; 
import com.example.TravelApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmailService emailService; 

    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password,
                              HttpSession session, Model model) {
        var user = userService.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get());
            session.setAttribute("userId", user.get().getId());
            session.setAttribute("userRole", user.get().getRole());
            session.setAttribute("userName", user.get().getName());

            if ("ADMIN".equals(user.get().getRole())) {
                return "redirect:/admin";
            }
            return "redirect:/";
        }

        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String name, @RequestParam String email,
                                 @RequestParam String password, @RequestParam String phone,
                                 Model model) {
        if (userService.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        User newUser = new User(name, email, password, "USER");
        newUser.setPhone(phone);
        userService.save(newUser);

        model.addAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ==========================================
    // REAL WORLD EMAIL FORGOT PASSWORD OPTION (FIXED FOR NO SMTP)
    // ==========================================

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, Model model) {
        var userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // රහස්‍ය ටෝකන් එකක් සහ Expiry කාලයක් දීම
            String token = java.util.UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(java.time.LocalDateTime.now().plusMinutes(15));
            userService.save(user);

            // 💡 Production එකේදීත් ලින්ක් එක වැඩ කරන්න සාපේක්ෂ (Relative) පාරක් හදමු
            String resetLink = "/reset-password?token=" + token;

            try {
                // 🎯 ඊමේල් සර්විස් එකට ඩේටා පාස් කරනවා (හැබැයි මේල් යන්නේ නැහැ, ක්‍රෑෂ් වෙන්නෙත් නැහැ)
                emailService.sendForgotPasswordEmail(email, user.getName(), resetLink);

                // 🌿 ඊමේල් යන්නේ නැති නිසා, යූසර්ට කෙලින්ම ස්ක්‍රීන් එකේ ක්ලික් කරලා පාස්වර්ඩ් මාරු කරන්න ලින්ක් එක පෙන්වනවා:
                model.addAttribute("success", "Password reset generation successful! [Bypass Mode]");
                model.addAttribute("bypassLink", resetLink);
                
            } catch (Exception e) {
                model.addAttribute("error", "System error handling request.");
            }

        } else {
            model.addAttribute("error", "Email address not found!");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        var userOptional = userService.findByResetToken(token);

        if (userOptional.isEmpty() || userOptional.get().getTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired password reset token!");
            return "forgot-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        var userOptional = userService.findByResetToken(token);

        if (userOptional.isEmpty() || userOptional.get().getTokenExpiry().isBefore(java.time.LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired token!");
            return "forgot-password";
        }

        User user = userOptional.get();
        userService.updatePassword(user, password);

        model.addAttribute("success", "Password reset successful! Please login with your new password.");
        return "login";
    }
}
