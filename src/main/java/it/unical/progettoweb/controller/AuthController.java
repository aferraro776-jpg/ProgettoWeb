package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.model.User;
import it.unical.progettoweb.service.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("La password è obbligatoria");
        }
        if (userDao.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider("LOCAL");
        userDao.save(user);
        return ResponseEntity.ok("Registrazione avvenuta");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }

        User user = userOpt.get();

        if ("GOOGLE".equals(user.getAuthProvider())) {
            return ResponseEntity.status(401).body("Questo account usa Google per accedere");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }

        String jwt = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        return userDao.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}