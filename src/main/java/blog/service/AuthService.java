package blog.service;

import blog.dto.auth.LoginRequest;
import blog.dto.auth.LoginResponse;
import blog.mapper.UserMapper;
import blog.model.User;
import blog.repository.UserRepository;
import blog.security.JwtService;
import blog.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public LoginResponse authenticate(LoginRequest request) {
    Authentication credentials =
            new UsernamePasswordAuthenticationToken(
                    request.login(),
                    request.password());
    authenticationManager.authenticate(credentials);

    User user = repository.findByEmail(request.login())
            .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED));

    String token = jwtService.generateToken(user.getId());

    return new LoginResponse(UserMapper.toEditResponse(user), token);
  }

  protected User getAuthenticatedUser() {
    UserDetailsImpl principal =
            (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

    return repository.findById(principal.getId())
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "User not authenticated"));
  }

}
