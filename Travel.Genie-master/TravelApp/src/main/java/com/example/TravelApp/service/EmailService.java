package com.example.TravelApp.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Spring Boot මඟින් application.properties වල සෙටින්ග්ස් මෙතනට auto සම්බන්ධ කරනවා
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 🎯 1. Booking එක සාර්ථක වුණාම යන ඊමේල් එක (ඔයා ලියපු එක ඒ විදිහටම තියෙනවා)
    public void sendBookingSuccessEmail(String toEmail, String userName, String packageName, double price) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("your-email@gmail.com"); // 💡 ඔයාගේ Gmail එක මෙතනට දාන්න
            helper.setTo(toEmail);
            helper.setSubject("Booking Confirmed! 🎉 - TravelApp");

            String htmlContent = "<div style=\"background-color: #1a1e21; padding: 30px; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #dee2e6; max-width: 600px; margin: 0 auto; border-radius: 8px;\">" +
                    "  <div style=\"background-color: #0f766e; color: #ffffff; padding: 20px; border-radius: 6px 6px 0 0; text-align: center; font-size: 22px; font-weight: bold;\">" +
                    "    ✓ Booking Successful!" +
                    "  </div>" +
                    "  <div style=\"padding: 20px 10px;\">" +
                    "    <p style=\"font-size: 16px; color: #ffffff;\">Hello " + userName + ",</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Thank you for booking with TravelApp! Your adventure has been successfully reserved and your details have been confirmed by our team.</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Below is a summary of your booked travel package. If you have any questions or need immediate assistance, feel free to contact us directly.</p>" +
                    "  </div>" +
                    "  <div style=\"background-color: #212529; border: 1px solid #2c3034; border-left: 4px solid #0f766e; padding: 20px; border-radius: 4px; margin-bottom: 25px;\">" +
                    "    <h4 style=\"margin-top: 0; color: #0f766e; font-size: 16px; margin-bottom: 12px;\">Your Booking Summary:</h4>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Package:</strong> " + packageName + "</p>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Total Paid:</strong> LKR " + String.format("%,.2f", price) + "</p>" +
                    "    <p style=\"margin: 6px 0; font-size: 14px;\"><strong style=\"color: #ffffff;\">Status:</strong> Confirmed</p>" +
                    "  </div>" +
                    "  <hr style=\"border: 0; border-top: 1px solid #2c3034; margin-bottom: 20px;\">" +
                    "  <div style=\"font-size: 13px; color: #6c757d; line-height: 1.6;\">" +
                    "    <p style=\"margin: 5px 0;\"><strong style=\"color: #adb5bd;\">Have questions?</strong> Check out our FAQ or visit our website: <a href=\"http://localhost:8080/\" style=\"color: #0f766e; text-decoration: none; font-weight: bold;\">TravelApp.com</a></p>" +
                    "    <p style=\"margin: 15px 0 5px 0; font-weight: bold; color: #adb5bd;\">Best regards,</p>" +
                    "    <p style=\"margin: 0; font-weight: bold; color: #ffffff;\">The TravelApp Team</p>" +
                    "  </div>" +
                    "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("📧 Beautiful HTML Booking Email Sent to: " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Failed to send HTML email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 🎯 2. Forgot Password සඳහා යන අලුත්ම ඊමේල් මෙතඩ් එක (WhatsApp පින්තූරයේ තීම් එකටම හැදුවා)
    public void sendForgotPasswordEmail(String toEmail, String userName, String resetLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("your-email@gmail.com"); // 💡 ඔයාගේ Gmail එක මෙතනට දාන්න
            helper.setTo(toEmail);
            helper.setSubject("🔒 Reset Your TravelApp Password");

            // 🎨 WhatsApp පින්තූරයට 100%ක් ගැලපෙන නිවැරදි Dark Mode HTML Layout එක
            String htmlContent = "<div style=\"background-color: #1a1e21; padding: 30px; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #dee2e6; max-width: 600px; margin: 0 auto; border-radius: 8px;\">" +

                    // Header (Green/Teal Bar)
                    "  <div style=\"background-color: #0f766e; color: #ffffff; padding: 20px; border-radius: 6px 6px 0 0; text-align: center; font-size: 22px; font-weight: bold;\">" +
                    "    ✓ We Received Your Request" +
                    "  </div>" +

                    // Body Content
                    "  <div style=\"padding: 20px 10px;\">" +
                    "    <p style=\"font-size: 16px; color: #ffffff;\">Hello " + userName + ",</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Thank you for contacting TravelApp! We have received your request to reset your password and our team has generated a secure gateway for you.</p>" +
                    "    <p style=\"font-size: 14px; line-height: 1.6; color: #adb5bd;\">Click the button below within the next 24 hours to securely update your credentials. If you did not make this request, you can safely ignore this email.</p>" +
                    "  </div>" +

                    // 🎯 Reset Password Button Layer (මැද තියෙන "Reset Password" බටන් එක)
                    "  <div style=\"text-align: center; margin: 30px 0;\">" +
                    "    <a href=\"" + resetLink + "\" style=\"background-color: #0f766e; color: #ffffff; padding: 14px 35px; border-radius: 6px; text-decoration: none; font-weight: bold; font-size: 15px; display: inline-block; box-shadow: 0 4px 6px rgba(0,0,0,0.2);\">" +
                    "      Reset Password" +
                    "    </a>" +
                    "  </div>" +

                    "  <hr style=\"border: 0; border-top: 1px solid #2c3034; margin-bottom: 20px;\">" +

                    // Footer Info
                    "  <div style=\"font-size: 13px; color: #6c757d; line-height: 1.6;\">" +
                    "    <p style=\"margin: 5px 0;\"><strong style=\"color: #adb5bd;\">Have questions?</strong> Check out our FAQ or visit our website: <a href=\"http://localhost:8080/\" style=\"color: #0f766e; text-decoration: none; font-weight: bold;\">TravelApp.com</a></p>" +
                    "    <p style=\"margin: 15px 0 5px 0; font-weight: bold; color: #adb5bd;\">Best regards,</p>" +
                    "    <p style=\"margin: 0; font-weight: bold; color: #ffffff;\">The TravelApp Support Team</p>" +
                    "  </div>" +

                    "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("🔒 Forgot Password HTML Email Sent Successfully to: " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Failed to send Forgot Password email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}