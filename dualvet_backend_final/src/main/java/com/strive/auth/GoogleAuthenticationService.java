package com.strive.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.strive.config.JwtService;
import com.strive.model.User;
import com.strive.model.UserRepository;
import com.strive.token.Token;
import com.strive.token.TokenRepository;

@Service
public class GoogleAuthenticationService {

    @Autowired
    private UserRepository userRepository;  // Repository to access User data

    @Autowired
    private TokenRepository tokenRepository; // Repository to manage tokens

    @Autowired
    private JwtService jwtService;  // Service to handle JWT token generation

    // Authenticate Google user by their email
    public AuthenticationResponse authenticateGoogleUser(String email) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // If the user is not found, return null or handle differently (e.g., throw an exception)
            return null;
        }

        // Get the appUser from the database
        User appUser = userOptional.get();

        // Convert appUser to UserDetails for JWT generation using the builder
        UserDetails userDetails = User.builder()
                .email(appUser.getEmail())  // Set the username (email)
                .password(appUser.getPassword() != null ? appUser.getPassword() : "") // Handle null password gracefully
                .role(appUser.getRole())  // Use the role from the User class (ROLE_USER, ROLE_ADMIN, etc.)
                .build();

        // Generate an access token and a refresh token for the user
        String accessToken = jwtService.generateToken(userDetails); // Generate access token
        String refreshToken = jwtService.generateRefreshToken(userDetails); // Generate refresh token

        // Revoke any existing tokens for the user
        revokeAllUserTokens(appUser);

        // Save the new access token
        saveUserToken(appUser, accessToken);

        // Create and populate the authentication response
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;  // Return the response with tokens
    }

    // Revoke all tokens for the user
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens); // Save the updated tokens in the database
    }

    // Save a new token for the user
    private void saveUserToken(User user, String token) {
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(newToken); // Save the new token in the database
    }
}
