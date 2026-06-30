package com.example.TravelApp.controller;

import com.example.TravelApp.model.ContactMessage;
import com.example.TravelApp.repository.ContactMessageRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

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
            // 🎯 1. මැසේජ් එක MySQL ඩේටාබේස් එකට ඔටෝ සේව් කිරීම
            ContactMessage contactMessage = new ContactMessage(name, email, message);
            messageRepository.save(contactMessage);

            // 🎯 2. එකම එක ලස්සන HTML ඊමේල් එකක් පමණක් යැවීම (User හට යන පණිවිඩය)
            MimeMessage userMessage = mailSender.createMimeMessage();
            MimeMessageHelper userHelper = new MimeMessageHelper(userMessage, true, "UTF-8");

            userHelper.setTo(email);
            userHelper.setSubject("We Received Your Message - TravelApp");

            String userHtmlContent = "<div style='background-color: #1a1a1a; padding: 30px; font-family: Arial, sans-serif; color: #ffffff;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #242424; border-radius: 8px; overflow: hidden;'>"
                    + "<div style='background-color: #0f766e; padding: 20px; text-align: center; font-weight: bold; font-size: 20px; color: white;'>✓ We Received Your Message</div>"
                    + "<div style='padding: 30px; line-height: 1.6; color: #cccccc;'>"
                    + "<p>Hello " + name + ",</p>"
                    + "<p>Thank you for contacting TravelApp! We have received your message and our team will review it shortly.</p>"
                    + "<p>We typically respond to inquiries within 24 hours during business days.</p>"
                    + "<br>"
                    + "<div style='background-color: #1f2937; padding: 20px; border-radius: 6px; border-left: 4px solid #0f766e;'>"
                    + "<h4 style='color: #0f766e; margin-top: 0;'>Your Message Summary:</h4>"
                    + "<p style='margin: 5px 0;'><strong>Subject:</strong> General Inquiry</p>"
                    + "<p style='margin: 5px 0;'><strong>Your Message:</strong> " + message + "</p>"
                    + "</div>"
                    + "<br>"
                    + "<p style='font-size: 13px; color: #888888;'>Have questions? Check out our FAQ or visit our website: <a href='#' style='color:#0f766e;'>TravelApp.com</a></p>"
                    + "<p style='margin-bottom: 0;'>Best regards,<br><strong>The TravelApp Support Team</strong></p>"
                    + "</div>"
                    + "</div>"
                    + "</div>";

            userHelper.setText(userHtmlContent, true);
            mailSender.send(userMessage);

            model.addAttribute("success", "Your message has been sent successfully!");

        } catch (Exception e) {
            model.addAttribute("error", "Failed to send message. Please try again.");
        }

        return "contact";
    }

    // 🎯 3. Admin Dashboard එකේ Inbox පිටුවට මැසේජ් ටික ලෝඩ් කරවන GetMapping එක
    @GetMapping("/admin/inbox")
    public String showAdminInbox(Model model) {
        model.addAttribute("messages", messageRepository.findAllByOrderBySubmittedAtDesc());
        return "admin/inbox"; // templates/admin/inbox.html පිටුව ලෝඩ් කරයි
    }
}
