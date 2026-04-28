package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        assert oidcUser != null;
        String email   = oidcUser.getEmail()      != null ? oidcUser.getEmail()      : "";
        String name    = oidcUser.getGivenName()   != null ? oidcUser.getGivenName()   : "";
        String surname = oidcUser.getFamilyName()  != null ? oidcUser.getFamilyName()  : "";

        Optional<User> existing = userDao.findByEmail(email);
        if (existing.isEmpty()) {
            User newUser = new User();
            newUser.setId(generateUniqueId());
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setPassword(null);
            newUser.setBirthDate(null);
            newUser.setAuthProvider("GOOGLE");
            newUser.setBanned(false);
            userDao.save(newUser);
        }

        // Recupera l'utente (esistente o appena salvato) per prendere l'id
        User user = userDao.findByEmail(email).orElseThrow();
        String jwt = jwtUtil.generateToken(email, "USER", user.getId());

        String redirectUrl = "http://localhost:4200/oauth2/callback?token=" + jwt;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    // genera un id tra 10000 e 99999 verificando che non esista già
    private int generateUniqueId() {
        int id;
        do {
            id = new Random().nextInt(89999) + 10000;
        } while (userDao.get(id).isPresent()); // riprova finché l'id è libero
        return id;
    }
}