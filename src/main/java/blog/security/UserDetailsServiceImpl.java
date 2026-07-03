package blog.security;

import blog.model.User;
import blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
          throws UsernameNotFoundException {

    User user = userRepository.findByEmail(username)
            .orElseThrow(() ->
                    new UsernameNotFoundException(username));

    return new UserDetailsImpl(user);
  }

  public UserDetailsImpl loadUserByUserId(Long userId)
          throws UsernameNotFoundException {

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new UsernameNotFoundException(userId.toString()));

    return new UserDetailsImpl(user);
  }

}
