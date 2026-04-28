package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.AdminDao;
import it.unical.progettoweb.dao.impl.BlacklistDao;
import it.unical.progettoweb.dao.impl.SellerDao;
import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.dto.response.UserDto;
import it.unical.progettoweb.dto.request.SellerRequest;
import it.unical.progettoweb.dto.request.UserRequest;
import it.unical.progettoweb.model.Admin;
import it.unical.progettoweb.model.Seller;
import it.unical.progettoweb.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDao userDao;
    private final SellerDao sellerDao;
    private final AdminDao adminDao;
    private final BlacklistDao blacklistDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    private final EmailService emailService;

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

    // ── controlla la data di nascita
    private void validaDataNascita(java.util.Date data) {
        if (data == null)
            throw new IllegalArgumentException("Data di nascita obbligatoria.");
        if (!Validation.checkDataNascita(SDF.format(data)))
            throw new IllegalArgumentException("Data di nascita non valida.");
    }

    // ── registrazione acquirente (con verifica OTP)
    public void registraUser(UserRequest dto) {
        if (!otpService.verifyOtp(dto.getEmail(), dto.getOtp()))
            throw new IllegalArgumentException("OTP non valido o scaduto.");

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
    public void registraSeller(SellerRequest dto) {
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

    // ── login — restituisce il token JWT
    public String login(String email, String password) {
        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("GOOGLE".equals(user.getAuthProvider()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Questo account usa Google per accedere.");
            if (!passwordEncoder.matches(password, user.getPassword()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
            return jwtUtil.generateToken(email, "USER");
        }

        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (!passwordEncoder.matches(password, seller.getPassword()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
            return jwtUtil.generateToken(email, "SELLER");
        }

        Optional<Admin> adminOpt = adminDao.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!passwordEncoder.matches(password, admin.getPassword()))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
            return jwtUtil.generateToken(email, "ADMIN");
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
    }

    // ── restituisce il profilo dell'utente autenticato dal token JWT
    public UserDto getMe(String authHeader) {
        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido o scaduto.");

        String email = jwtUtil.extractEmail(token);

        return userDao.findByEmail(email)
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getSurname(),
                        user.getEmail(),
                        user.getBirthDate(),
                        user.getAuthProvider()
                ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato."));
    }

    // ── invia OTP per la registrazione
    public void inviaOtpRegistrazione(String email) {
        if (userDao.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Email già registrata.");
        String code = otpService.generateOtp(email);
        emailService.sendOtp(email, code, "Registrazione");
    }

    // ── invia OTP per il recupero password
    public void inviaOtpRecuperoPassword(String email) {
        if (userDao.findByEmail(email).isEmpty())
            throw new IllegalArgumentException("Email non trovata.");
        String code = otpService.generateOtp(email);
        emailService.sendOtp(email, code, "Recupero password");
    }

    // ── reset password con verifica OTP
    public void resetPassword(String email, String otp, String newPassword) {
        if (!otpService.verifyOtp(email, otp))
            throw new IllegalArgumentException("OTP non valido o scaduto.");

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));

        validaPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.update(user);
    }
}