package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.UserDao;
import it.unical.progettoweb.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();
        String email;
        String name;
        String surname;

        if (principal instanceof OidcUser oidcUser) {
            email   = oidcUser.getEmail() != null ? oidcUser.getEmail() : "";
            name    = oidcUser.getGivenName() != null ? oidcUser.getGivenName() : "";
            surname = oidcUser.getFamilyName() != null ? oidcUser.getFamilyName() : "";
        } else if (principal instanceof OAuth2User oauth2User) {
            email   = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email") : "";
            name    = oauth2User.getAttribute("given_name") != null ? oauth2User.getAttribute("given_name") : "";
            surname = oauth2User.getAttribute("family_name") != null ? oauth2User.getAttribute("family_name") : "";
        } else {
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:4200/login?error=true");
            return;
        }

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
            userDao.save(newUser);
        }

        String jwt = jwtUtil.generateToken(email);
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:4200/oauth2/callback?token=" + jwt);
    }

    private int generateUniqueId() {
        return new Random().nextInt(89997) + 10001;
    }
}