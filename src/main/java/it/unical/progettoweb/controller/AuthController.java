package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.send.SellerDto;
import it.unical.progettoweb.dto.send.UserDto;
import it.unical.progettoweb.model.User;
import it.unical.progettoweb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import it.unical.progettoweb.service.EmailService;
import it.unical.progettoweb.service.OtpService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserDao userDao;

    @GetMapping("/hash")
    public String hash() {
        return passwordEncoder.encode("Admin123!");
    }

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto dto) {
        try {
            authService.registraUser(dto);
            return ResponseEntity.ok("Registrazione avvenuta con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@RequestBody SellerDto dto) {
        try {
            authService.registraSeller(dto);
            return ResponseEntity.ok("Registrazione venditore avvenuta con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.getMe(authHeader));
    }

    @PostMapping("/register/request-otp")
    public ResponseEntity<String> requestRegistrationOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (userDao.findByEmail(email).isPresent())
            return ResponseEntity.badRequest().body("Email già registrata.");
        String code = otpService.generateOtp(email);
        emailService.sendOtp(email, code, "Registrazione");
        return ResponseEntity.ok("OTP inviato.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (userDao.findByEmail(email).isEmpty())
            return ResponseEntity.badRequest().body("Email non trovata.");
        String code = otpService.generateOtp(email);
        emailService.sendOtp(email, code, "Recupero password");
        return ResponseEntity.ok("OTP inviato.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        String email   = body.get("email");
        String otp     = body.get("otp");
        String newPass = body.get("newPassword");

        if (!otpService.verifyOtp(email, otp))
            return ResponseEntity.badRequest().body("OTP non valido o scaduto.");

        User user = userDao.findByEmail(email)
                .orElse(null);

        if (user == null)
            return ResponseEntity.badRequest().body("Utente non trovato.");

        user.setPassword(passwordEncoder.encode(newPass));
        userDao.update(user);

        return ResponseEntity.ok("Password aggiornata.");
    }
}