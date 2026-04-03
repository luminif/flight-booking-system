package ru.java.airport.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.java.airport.entity.Role;
import ru.java.airport.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtEntityFactory {
    public static JwtEntity create(User user) {
        return new JwtEntity(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}
