package it.unical.progettoweb.controller;

import it.unical.progettoweb.dto.send.UserDto;
import it.unical.progettoweb.model.User;
import it.unical.progettoweb.service.JwtUtil;
import it.unical.progettoweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // GET /api/user/me
    // Restituisce i dati del profilo dell'utente
    // L'email viene estratta dal token JWT nell'header, non dal body
    @GetMapping("/me")
    public ResponseEntity<?> getProfilo(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = estraiEmail(authHeader);
            User user = userService.getUtenteByEmail(email);

            // costruisce il DTO di risposta, la password non viene mai inclusa
            UserDto dto = new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getBirthDate(),
                    user.getAuthProvider()
            );
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/user/me
    // Aggiorna nome, cognome, email e data di nascita dell'utente autenticato
    // Body: { "name": "...", "surname": "...", "email": "...", "birthDate": "yyyy-MM-dd" }
    @PutMapping("/me")
    public ResponseEntity<String> aggiornaProfilo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserDto dto) {
        try {
            String email = estraiEmail(authHeader);
            userService.aggiornaProfilo(email, dto);
            return ResponseEntity.ok("Profilo aggiornato con successo.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/user/me/password
    // cambia la password dell'utente
    // Body: { "oldPassword": "...", "newPassword": "..." }
    @PutMapping("/me/password")
    public ResponseEntity<String> cambiaPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> body) {
        try {
            String email = estraiEmail(authHeader);
            String oldPassword = body.get("oldPassword");
            String newPassword = body.get("newPassword");

            // controlla che entrambi i campi siano presenti nel body
            if (oldPassword == null || newPassword == null)
                return ResponseEntity.badRequest()
                        .body("I campi oldPassword e newPassword sono obbligatori.");

            userService.cambiaPassword(email, oldPassword, newPassword);
            return ResponseEntity.ok("Password aggiornata con successo.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/user/me
    // Cancella l'account dell'utente
    @DeleteMapping("/me")
    public ResponseEntity<String> cancellaAccount(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String email = estraiEmail(authHeader);
            userService.cancellaAccount(email);
            return ResponseEntity.ok("Account eliminato con successo.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // metodo privato riutilizzato da tutti gli endpoint
    // estrae l'email dal token JWT rimuovendo il prefisso "Bearer "
    private String estraiEmail(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }
}