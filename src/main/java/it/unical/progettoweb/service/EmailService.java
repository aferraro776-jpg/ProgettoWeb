package it.unical.progettoweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String to, String code, String purpose) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Il tuo codice OTP - " + purpose);
        msg.setText("Il tuo codice è: " + code + "\nScade tra 10 minuti.");
        mailSender.send(msg);
    }
}