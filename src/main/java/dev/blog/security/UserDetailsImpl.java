package dev.blog.security;

import dev.blog.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public record UserDetailsImpl(User user) implements UserDetails {

    public UserDetailsImpl {
        Objects.requireNonNull(user);
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @NonNull String getUsername() {
        return user.getEmail();
    }

    @Override
    public @NonNull String getPassword() {
        return user.getPassword();
    }

}
