package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.BlacklistDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.model.Admin;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserDao userDao;
    private final SellerDao sellerDao;
    private final AdminDao adminDao;
    private final BlacklistDao blacklistDao;

    private final Random random = new Random();

    // genera un id per il nuovo admin se non esiste gia
    private int generateAdminId() {
        int id;
        do {
            id = random.nextInt(89999) + 10000;
        } while (adminDao.get(id).isPresent());
        return id;
    }

    // banna l'utente e aggiunge la mail alla blacklist
    public void banUser(String email) {

        // cerca prima tra gli user
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isBanned())
                throw new IllegalStateException("Utente già bannato.");
            user.setBanned(true);
            userDao.update(user);
            blacklistDao.ban(email);
            return;
        }

        // cerca tra i seller
        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (seller.isBanned())
                throw new IllegalStateException("Venditore già bannato.");
            seller.setBanned(true);
            sellerDao.update(seller);
            blacklistDao.ban(email);
            return;
        }

        throw new IllegalArgumentException("Nessun utente trovato con questa email.");
    }

    // unban, rimuove la mail dalla blacklist
    public void unbanUser(String email) {
        if (!blacklistDao.isBanned(email))
            throw new IllegalStateException("L'utente non risulta bannato.");

        blacklistDao.unban(email);

        // ripristina il flag is_banned sull'entità corrispondente
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setBanned(false);
            userDao.update(user);
            return;
        }

        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            seller.setBanned(false);
            sellerDao.update(seller);
        }
        // Se l'entità non esiste più in nessuna tabella, la blacklist è già stata pulita
    }

    // trasforma un User in Admin, copia i dati in admins, poi cancella dalla tabella users
    public void promuoviAdAdmin(String email) {

        // solo gli user possono essere promossi, i seller no
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nessun utente trovato con questa email. " +
                                "Solo gli acquirenti possono essere promossi ad amministratore."));

        // Costruisce il nuovo admin copiando i campi di person
        Admin admin = new Admin();
        admin.setId(generateAdminId());
        admin.setName(user.getName());
        admin.setSurname(user.getSurname());
        admin.setEmail(user.getEmail());

        // Mantiene la stessa password già hashata con BCrypt
        admin.setPassword(user.getPassword());

        // salva in admins e cancella da users
        adminDao.save(admin);
        userDao.delete(user.getId());
    }
}