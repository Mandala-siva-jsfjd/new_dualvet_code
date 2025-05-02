package com.strive.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strive.config.JwtService;
import com.strive.exception.AccountLockedException;
import com.strive.exception.EmailAlreadyExistsException;
import com.strive.loginaduit.LoginAduit;
import com.strive.loginaduit.LoginAuditRepository;
import com.strive.model.Flag;
import com.strive.model.User;
import com.strive.model.UserRepository;
import com.strive.token.Token;
import com.strive.token.TokenRepository;
import com.strive.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import javax.crypto.Cipher;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService 
{

  private final UserRepository repository;	
  private final TokenRepository tokenRepository;	
  private final PasswordEncoder passwordEncoder;	
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
   User user;
   private final LoginAuditRepository repo;

  
  public void register(RegisterRequest request) throws EmailAlreadyExistsException {
	    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
	    if (existingUser.isPresent()) {
	        throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
	    }

	    var user = User.builder()
	            .firstname(request.getFirstname())
	            .lastname(request.getLastname())
	            .email(request.getEmail())
	            .password(passwordEncoder.encode(request.getPassword()))
	            .role(request.getRole())
	            .contactNumber(request.getContactNumber())
	            .location(request.getLocation())
	            .isActiveFlag(Flag.ACTIVE)
	            .build();
	    repository.save(user);
	}

  public AuthenticationResponse authenticate(AuthenticationRequest request, String clientIp) {
	    try {
	        // Ensure password contains both IV and encrypted parts
	        String[] parts = request.getPassword().split(":");
	        if (parts.length < 2) {
	            throw new IllegalArgumentException("Invalid password format.");
	        }
	        String iv = parts[0];
	        String encryptedPassword = parts[1];

	        // Fetch user from the repository by email
	        var user = repository.findByEmail(request.getEmail())
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

	        // Check if the user account is locked
	        if (user.getIsActiveFlag() == Flag.INACTIVE) {
	            throw new AccountLockedException("Your account is locked due to multiple failed login attempts.");
	        }

	        // Decrypt the password using the provided IV
	        String decryptedPassword = decryptPassword(encryptedPassword, iv);

	        // Authenticate the user with decrypted password
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        request.getEmail(), decryptedPassword
	                )
	                
	        );

	        // Reset failed login attempts on successful login
	        user.resetFailedLoginAttempts();
	        repository.save(user);

	        // Log the login attempt with the user's IP address
	        LoginAduit audit = new LoginAduit();
	        audit.setEmail(request.getEmail());
	        audit.setLoginTime(LocalDateTime.now());
	        audit.setUserIp(clientIp); // Save the client IP
	        repo.save(audit);

	        // Generate access and refresh JWT tokens
	        String jwtToken = jwtService.generateToken(user);
	        String refreshToken = jwtService.generateRefreshToken(user);

	        // Revoke previous tokens and save the new one
	        revokeAllUserTokens(user);
	        saveUserToken(user, jwtToken);

	        return AuthenticationResponse.builder()
	                .accessToken(jwtToken)
	                .refreshToken(refreshToken)
	                .build();

	    } catch (AccountLockedException e) {
	        // Handle account locked exception explicitly
	        throw e;

	    } catch (Exception e) {
	        // Handle general authentication failures
	        var user = repository.findByEmail(request.getEmail()).orElse(null);

	        if (user != null) {
	            // Increment failed login attempts
	            user.incrementFailedLoginAttempts();

	            // Lock the account if login attempts exceed 3
	            if (user.getFailedLoginAttempts() >= 3) {
	                user.setIsActiveFlag(Flag.INACTIVE);
	            }
	            repository.save(user);
	            
	            LoginAduit audit = new LoginAduit();
	            audit.setEmail(request.getEmail());
	            audit.setLoginFail(LocalDateTime.now());
	            audit.setUserIp(clientIp);
	            repo.save(audit);
	            
	        }
	        throw new RuntimeException("Invalid credentials or account locked.", e);
	    }
	}




  private String decryptPassword(String encryptedPassword, String ivString) throws Exception {
	    System.out.println("Encrypted Password: " + encryptedPassword);
	    System.out.println("IV: " + ivString);

	    byte[] decodedPassword = Base64.getDecoder().decode(encryptedPassword);
	    byte[] iv = Base64.getDecoder().decode(ivString);

	    byte[] keyBytes = new byte[32];
	    for (int i = 0; i < 32; i++) {
	        keyBytes[i] = (byte) i;
	    }
	    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

	    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	    GCMParameterSpec spec = new GCMParameterSpec(128, iv);
	    cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

	    byte[] decrypted = cipher.doFinal(decodedPassword);

	    return new String(decrypted, StandardCharsets.UTF_8);
	}


  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}