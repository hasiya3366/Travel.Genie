package com.example.TravelApp.repository;

import com.example.TravelApp.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    // අලුත්ම පණිවිඩ ලැයිස්තුවේ ඉහළටම එන විදිහට ලබා ගැනීමට
    List<ContactMessage> findAllByOrderBySubmittedAtDesc();
}