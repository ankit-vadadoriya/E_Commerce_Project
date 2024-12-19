package com.example.e_commerce1.configuration;

import com.example.e_commerce1.model.Role;
import com.example.e_commerce1.model.User;
import com.example.e_commerce1.repository.RoleRepository;
import com.example.e_commerce1.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttributes().get("email").toString();

        Optional<User> existingUser = userRepository.findUserByEmail(email);
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString());
            user.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
            user.setEmail(email);

            Role defaultRole = roleRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Role with ID 2 not found."));
            user.setRoles(List.of(defaultRole));

            try {
                userRepository.save(user);
            } catch (Exception e) {
                System.err.println("Error saving user: " + e.getMessage());
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/error");
                return;
            }
        }

        String redirectUrl = "/";
        if (existingUser.isPresent() && existingUser.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"))) {
            redirectUrl = "/admin";
        }

        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
    }
}

