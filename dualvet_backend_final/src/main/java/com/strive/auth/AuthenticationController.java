package com.strive.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.strive.exception.EmailAlreadyExistsException;
import com.strive.model.Flag;
import com.strive.model.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service; // Lombok will inject this automatically
    private final GoogleAuthenticationService googleService; // Lombok will inject this automatically
    private final UserRepository repository;
   
    //private String googleClientId="435273669571-b9f9tjfjumd0hfgf7kjnp1g4auvfj204.apps.googleusercontent.com";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws EmailAlreadyExistsException {
        service.register(request);
        String message = "User successfully registered: ";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {

        // Capture the IP address
        String clientIp = extractIp(servletRequest); // Correctly use extractIp method
        System.out.println("Login attempt from IP: " + clientIp);

        // Pass the request and IP to the service method
        AuthenticationResponse response = service.authenticate(request, clientIp);
        return ResponseEntity.ok(response);
    }

    // Extract IP Address from the request
    private String extractIp(HttpServletRequest request) {
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");

        if (Objects.nonNull(clientXForwardedForIp)) {
            return parseXForwardedHeader(clientXForwardedForIp);
        }
        return request.getRemoteAddr();
    }

    // Parse the 'x-forwarded-for' header to get the original IP
    private String parseXForwardedHeader(String headerValue) {
        if (headerValue == null || headerValue.isEmpty()) {
            return "";
        }
        // Get the first IP in case of multiple proxies
        String[] ipAddresses = headerValue.split(",");
        return ipAddresses[0].trim();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String googleToken = request.get("googleToken");

        try {
            // Create the GoogleIdTokenVerifier with Gson instead of JacksonFactory
            JsonFactory jsonFactory = new GsonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            // Verify Google Token
            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get user's Google email
                String email = payload.getEmail();
                
                // Fetch user from the repository by email
                var user = repository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

                // Check if the user account is locked
                if (user.getIsActiveFlag() == Flag.INACTIVE) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account is locked. Please contact support.");
                }

                // Authenticate Google user
                AuthenticationResponse responseEntity = googleService.authenticateGoogleUser(email);

                if (responseEntity != null) {
                    return ResponseEntity.ok(responseEntity);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Google account is not registered.");
                }
            
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google login failed. Please try again.");
        }
    }
}
