package com.example.TravelApp.controller;

import com.example.TravelApp.model.ContactMessage;
import com.example.TravelApp.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageRepository messageRepository; // 💡 Repository එක Inject කිරීම

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

    @PostMapping("/contact/send")
    public String sendContactMessage(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model) {

        try {
            // 🎯 1. මැසේජ් එක MySQL ඩේටාබේස් එකට ඔටෝ සේව් කිරීම (මේක විතරක් ක්‍රියාත්මක වෙනවා)
            ContactMessage contactMessage = new ContactMessage(name, email, message);
            messageRepository.save(contactMessage);

            // ඊමේල් යැවීමේ කොටස සම්පූර්ණයෙන්ම ඉවත් කර කෙලින්ම Success පණිවිඩය පෙන්වීම
            model.addAttribute("success", "Your message has been sent successfully!");

        } catch (Exception e) {
            model.addAttribute("error", "Failed to send message. Please try again.");
        }

        return "contact";
    }

    // 🎯 2. Admin Dashboard එකේ Inbox පිටුවට මැසේජ් ටික ලෝඩ් කරවන GetMapping එක
    @GetMapping("/admin/inbox")
    public String showAdminInbox(Model model) {
        model.addAttribute("messages", messageRepository.findAllByOrderBySubmittedAtDesc());
        return "admin/inbox"; // templates/admin/inbox.html පිටුව ලෝඩ් කරයි
    }
}
