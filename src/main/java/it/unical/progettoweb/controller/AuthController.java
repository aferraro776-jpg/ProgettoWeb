package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.SellerDto;
import it.unical.progettoweb.dto.UserDto;
import it.unical.progettoweb.model.Admin;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.model.User;
import it.unical.progettoweb.service.JwtUtil;
import it.unical.progettoweb.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDao userDao;
    private final SellerDao sellerDao;
    private final AdminDao adminDao;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto dto) {
        try {
            registrationService.registraUser(dto);
            return ResponseEntity.ok("Registrazione avvenuta con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@RequestBody SellerDto dto) {
        try {
            registrationService.registraSeller(dto);
            return ResponseEntity.ok("Registrazione venditore avvenuta con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        // cerca in users
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("GOOGLE".equals(user.getAuthProvider()))
                return ResponseEntity.status(401).body("Questo account usa Google per accedere.");
            if (!passwordEncoder.matches(password, user.getPassword()))
                return ResponseEntity.status(401).body("Credenziali non valide.");
            return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(email, "USER")));
        }

        // cerca in sellers
        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (!passwordEncoder.matches(password, seller.getPassword()))
                return ResponseEntity.status(401).body("Credenziali non valide.");
            return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(email, "SELLER")));
        }

        // cerca in admins
        Optional<Admin> adminOpt = adminDao.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches(password, admin.getPassword()))
                return ResponseEntity.status(401).body("Credenziali non valide.");
            return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(email, "ADMIN")));
        }

        // non trovato in nessuna tabella
        return ResponseEntity.status(401).body("Credenziali non valide.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token))
            return ResponseEntity.status(401).body("Token non valido o scaduto.");

        String email = jwtUtil.extractEmail(token);

        return userDao.findByEmail(email)
                .map(user -> ResponseEntity.ok(new UserDto(
                        user.getId(), user.getName(), user.getSurname(),
                        user.getEmail(), user.getBirthDate(), user.getAuthProvider()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}