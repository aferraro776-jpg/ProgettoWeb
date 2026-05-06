package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.BlacklistDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.request.SellerRequest;
import it.unical.progettoweb.model.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerDao sellerDao;
    private final UserDao userDao;
    private final AdminDao adminDao;
    private final BlacklistDao blacklistDao;
    private final PasswordEncoder passwordEncoder;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    // valida la nuova email (prima verifica se il formato è corretto, poi verifica che non si trova nella blacklist,
    // infine verifica che non esista già un account con questa email.
    // esclude l'email attuale così l'utente può "riconfermare" la propria senza errori
    private void validaEmailModifica(String nuovaEmail, String emailAttuale) {
        if (!Validation.checkEmail(nuovaEmail))
            throw new IllegalArgumentException("Formato email non valido.");

        if (blacklistDao.isBanned(nuovaEmail))
            throw new IllegalArgumentException("Si è verificato un errore. La mail che stai cercando di usare è stata bannata.");

        if (!nuovaEmail.equalsIgnoreCase(emailAttuale)) {
            if (userDao.existsByEmail(nuovaEmail) ||
                    sellerDao.existsByEmail(nuovaEmail) ||
                    adminDao.existsByEmail(nuovaEmail))
                throw new IllegalArgumentException("Questa Email è gia associata ad un account.");
        }
    }

    // valida nome e cognome
    private void validaGeneralita(String nome, String cognome) {
        if (!Validation.checkNome(nome))
            throw new IllegalArgumentException("Nome non valido (minimo 3 caratteri).");
        if (!Validation.checkCognome(cognome))
            throw new IllegalArgumentException("Cognome non valido (minimo 3 caratteri).");
    }

    // valida la data di nascita
    private void validaDataNascita(java.util.Date data) {
        if (data == null)
            throw new IllegalArgumentException("Data di nascita obbligatoria.");
        if (!Validation.checkDataNascita(SDF.format(data)))
            throw new IllegalArgumentException("Data di nascita non valida.");
    }

    // recupera il venditore tramite email estratta dal JWT
    public Seller getSellerByEmail(String email) {
        return sellerDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Venditore non trovato."));
    }

    // aggiorna nome, cognome, email e data di nascita
    // la p.iva è immutabile
    public void aggiornaProfilo(String emailDalToken, SellerRequest dto) {
        Seller seller = getSellerByEmail(emailDalToken);

        validaGeneralita(dto.getName(), dto.getSurname());
        validaEmailModifica(dto.getEmail(), emailDalToken);
        validaDataNascita(dto.getBirthDate());

        seller.setName(dto.getName());
        seller.setSurname(dto.getSurname());
        seller.setEmail(dto.getEmail());
        seller.setBirthDate(dto.getBirthDate());
        sellerDao.update(seller);
    }

    // cambia la password verificando prima quella vecchia
    public void cambiaPassword(String emailDalToken, String oldPassword, String newPassword) {
        Seller seller = getSellerByEmail(emailDalToken);

        // verifica che la vecchia password sia corretta
        if (!passwordEncoder.matches(oldPassword, seller.getPassword()))
            throw new IllegalArgumentException("La vecchia password non è corretta.");

        // valida la nuova password con gli stessi criteri della registrazione
        String errori = Validation.getErrorePassword(newPassword);
        if (errori != null)
            throw new IllegalArgumentException(errori);

        // aggiorna con la nuova password hashata
        seller.setPassword(passwordEncoder.encode(newPassword));
        sellerDao.update(seller);
    }

    // cancella l'account del venditore
    public void cancellaAccount(String emailDalToken) {
        Seller seller = getSellerByEmail(emailDalToken);
        sellerDao.delete(seller.getId());
    }
}