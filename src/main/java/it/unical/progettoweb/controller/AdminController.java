package it.unical.progettoweb.controller;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.BlacklistDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.BlacklistRequestDto;
import it.unical.progettoweb.model.Admin;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.model.User;
import it.unical.progettoweb.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// ho aggiunto un v9 insert admin nella db migration, se serve usate quel token per fare i test
// la password corrispondente è Admin123!

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // tutti gli endpoint di questo controller richiedono ruolo admin
public class AdminController {

    private final AdminService adminService;
    private final UserDao userDao;
    private final SellerDao sellerDao;
    private final AdminDao adminDao;
    private final BlacklistDao blacklistDao;

    // GET /api/admin/users
    // Restituisce la lista di tutti gli acquirenti registrati
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userDao.getAll());
    }

    // GET /api/admin/sellers
    // Restituisce la lista di tutti i venditori registrati
    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellers() {
        return ResponseEntity.ok(sellerDao.getAll());
    }

    // GET /api/admin/admins
    // Restituisce la lista di tutti gli amministratori
    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminDao.getAll());
    }

    // GET /api/admin/blacklist
    // Restituisce la lista di tutte le email in blacklist
    @GetMapping("/blacklist")
    public ResponseEntity<List<String>> getBlacklist() {
        return ResponseEntity.ok(blacklistDao.getAll());
    }

    // POST /api/admin/ban
    // Banna un utente: lo aggiunge alla blacklist e setta is_banned=true
    // Body: { "email": "utente@example.com" }
    @PostMapping("/ban")
    public ResponseEntity<String> banUser(@RequestBody BlacklistRequestDto request) {
        try {
            adminService.banUser(request.getEmail());
            return ResponseEntity.ok("Utente bannato con successo.");
        } catch (IllegalArgumentException e) {
            // Email non trovata in nessuna tabella
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Utente già bannato
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /api/admin/unban
    // Rimuove il ban: elimina dalla blacklist e setta is_banned=false
    // Body: { "email": "utente@example.com" }
    @PostMapping("/unban")
    public ResponseEntity<String> unbanUser(@RequestBody BlacklistRequestDto request) {
        try {
            adminService.unbanUser(request.getEmail());
            return ResponseEntity.ok("Ban rimosso con successo.");
        } catch (IllegalStateException e) {
            // Utente non risulta bannato
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  DELETE /api/admin/users/{id}
    // Cancella un acquirente dal sistema
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (userDao.get(id).isEmpty())
            return ResponseEntity.notFound().build();
        userDao.delete(id);
        return ResponseEntity.ok("Utente eliminato.");
    }

    // DELETE /api/admin/sellers/{id}
    // Cancella un venditore dal sistema
    @DeleteMapping("/sellers/{id}")
    public ResponseEntity<String> deleteSeller(@PathVariable int id) {
        if (sellerDao.get(id).isEmpty())
            return ResponseEntity.notFound().build();
        sellerDao.delete(id);
        return ResponseEntity.ok("Venditore eliminato.");
    }

    // POST /api/admin/promote
    // Promuove un User ad Admin: copia i dati in admins, cancella da users
    // Body: { "email": "utente@example.com" }
    @PostMapping("/promote")
    public ResponseEntity<String> promoteToAdmin(@RequestBody BlacklistRequestDto request) {
        try {
            adminService.promuoviAdAdmin(request.getEmail());
            return ResponseEntity.ok("Utente promosso ad amministratore.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}