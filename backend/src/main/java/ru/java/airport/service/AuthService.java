package ru.java.airport.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.java.airport.entity.User;
import ru.java.airport.security.JwtTokenProvider;
import ru.java.airport.web.dto.auth.JwtRequest;
import ru.java.airport.web.dto.auth.JwtResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );

        User user = userService.getByUsername(loginRequest.username());

        String accessToken = jwtTokenProvider.createAccessToken(
            user.getId(),
            user.getUsername(),
            user.getRoles()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken(
            user.getId(),
            user.getUsername()
        );

        return new JwtResponse(
            user.getId(),
            user.getUsername(),
            accessToken,
            refreshToken
        );
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
