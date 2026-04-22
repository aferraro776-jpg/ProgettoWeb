package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.BlacklistDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.send.SellerDto;
import it.unical.progettoweb.dto.send.UserDto;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserDao userDao;
    private final SellerDao sellerDao;
    private final AdminDao adminDao;
    private final BlacklistDao blacklistDao;
    private final PasswordEncoder passwordEncoder;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private final Random random = new Random();

    // ── genera id univoco per utenti (10000-99999)
    private int generateUserId() {
        int id;
        do {
            id = random.nextInt(89999) + 10000;
        } while (userDao.get(id).isPresent());
        return id;
    }

    // ── genera id univoco per venditori (10000-99999)
    private int generateSellerId() {
        int id;
        do {
            id = random.nextInt(89999) + 10000;
        } while (sellerDao.get(id).isPresent());
        return id;
    }

    // ── controlla email: formato, blacklist, unicità su tutte le tabelle
    private void validaEmail(String email) {
        if (!Validation.checkEmail(email))
            throw new IllegalArgumentException("Formato email non valido.");

        if (blacklistDao.isBanned(email))
            throw new IllegalArgumentException("Email non autorizzata alla registrazione.");

        if (userDao.existsByEmail(email) || sellerDao.existsByEmail(email) || adminDao.existsByEmail(email))
            throw new IllegalArgumentException("Email già registrata.");
    }

    // ── controlla nome e cognome
    private void validaGeneralita(String nome, String cognome) {
        if (!Validation.checkNome(nome))
            throw new IllegalArgumentException("Nome non valido (minimo 3 caratteri).");
        if (!Validation.checkCognome(cognome))
            throw new IllegalArgumentException("Cognome non valido (minimo 3 caratteri).");
    }

    // ── controlla la password con tutti i criteri di getErrorePassword
    private void validaPassword(String password) {
        String errori = Validation.getErrorePassword(password);
        if (errori != null)
            throw new IllegalArgumentException(errori);
    }

    // ── controlla la data di nascita: SDF.format() produce "yyyy-MM-dd" compatibile con la regex
    private void validaDataNascita(java.util.Date data) {
        if (data == null)
            throw new IllegalArgumentException("Data di nascita obbligatoria.");
        if (!Validation.checkDataNascita(SDF.format(data)))
            throw new IllegalArgumentException("Data di nascita non valida.");
    }

    // ── registrazione acquirente
    public void registraUser(UserDto dto) {
        validaEmail(dto.getEmail());
        validaGeneralita(dto.getName(), dto.getSurname());
        validaPassword(dto.getPassword());
        validaDataNascita(dto.getBirthDate());

        User user = new User();
        user.setId(generateUserId());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        user.setAuthProvider("LOCAL");
        user.setBanned(false);

        userDao.save(user);
    }

    // ── registrazione venditore
    public void registraSeller(SellerDto dto) {
        validaEmail(dto.getEmail());
        validaGeneralita(dto.getName(), dto.getSurname());
        validaPassword(dto.getPassword());
        validaDataNascita(dto.getBirthDate());

        if (dto.getVatNumber() == null || dto.getVatNumber().isBlank())
            throw new IllegalArgumentException("Partita IVA obbligatoria per i venditori.");

        if (sellerDao.existsByVatNumber(dto.getVatNumber()))
            throw new IllegalArgumentException("Partita IVA già registrata.");

        Seller seller = new Seller();
        seller.setId(generateSellerId());
        seller.setName(dto.getName());
        seller.setSurname(dto.getSurname());
        seller.setEmail(dto.getEmail());
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));
        seller.setBirthDate(dto.getBirthDate());
        seller.setVatNumber(dto.getVatNumber());
        seller.setBanned(false);

        sellerDao.save(seller);
    }
}