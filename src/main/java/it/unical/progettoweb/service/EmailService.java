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

    // per la mail del vincitore delle aste
    public void sendAuctionWon(String to, String postTitle, double amount) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Hai vinto l'asta - " + postTitle);
        msg.setText("Congratulazioni! Hai vinto l'asta per l'immobile \"" + postTitle +
                "\" con un'offerta di € " + String.format("%.2f", amount) + ".\n" +
                "Il venditore ti contatterà a breve.");
        mailSender.send(msg);
    }
}