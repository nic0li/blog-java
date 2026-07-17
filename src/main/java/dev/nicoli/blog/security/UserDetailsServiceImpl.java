package dev.nicoli.blog.security;

import dev.nicoli.blog.entity.User;
import dev.nicoli.blog.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username)
            throws UsernameNotFoundException {

        User user = repository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));

        return toUserDetails(user);
    }

    public @NonNull UserDetailsImpl loadUserById(@NonNull Long id)
            throws UsernameNotFoundException {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException(id.toString()));

        return toUserDetails(user);
    }

    private UserDetailsImpl toUserDetails(User user) {
        return new UserDetailsImpl(user);
    }

}
