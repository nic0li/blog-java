package dev.nicoli.blog.security;

public interface JwtService {
    String generateToken(Long userId);

    boolean validateToken(String token, UserDetailsImpl userDetails);

    String extractUser(String token);
}
