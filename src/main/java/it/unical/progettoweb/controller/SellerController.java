package it.unical.progettoweb.controller;

import it.unical.progettoweb.dto.SellerDto;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.service.JwtUtil;
import it.unical.progettoweb.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerController {

    private final SellerService sellerService;
    private final JwtUtil jwtUtil;

    // GET /api/seller/me
    // Restituisce i dati del profilo del venditore
    // L'email viene estratta dal token JWT nell'header, non dal body
    @GetMapping("/me")
    public ResponseEntity<?> getProfilo(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = estraiEmail(authHeader);
            Seller seller = sellerService.getSellerByEmail(email);

            // costruisce il DTO di risposta
            SellerDto dto = new SellerDto(
                    seller.getId(),
                    seller.getName(),
                    seller.getSurname(),
                    seller.getEmail(),
                    seller.getVatNumber(),
                    seller.getBirthDate()
            );
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/seller/me
    // Aggiorna nome, cognome, email e data di nascita del venditore
    // La partita IVA non può essere modificata
    // Body: { "name": "...", "surname": "...", "email": "...", "birthDate": "yyyy-MM-dd" }
    @PutMapping("/me")
    public ResponseEntity<String> aggiornaProfilo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SellerDto dto) {
        try {
            String email = estraiEmail(authHeader);
            sellerService.aggiornaProfilo(email, dto);
            return ResponseEntity.ok("Profilo aggiornato con successo.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/seller/me/password
    // Cambia la password del venditore autenticato
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

            sellerService.cambiaPassword(email, oldPassword, newPassword);
            return ResponseEntity.ok("Password aggiornata con successo.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/seller/me
    // Cancella l'account del venditore
    @DeleteMapping("/me")
    public ResponseEntity<String> cancellaAccount(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String email = estraiEmail(authHeader);
            sellerService.cancellaAccount(email);
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