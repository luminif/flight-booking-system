package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsAuthenticated;
import ru.java.airport.service.UserService;
import ru.java.airport.utils.Utils;
import ru.java.airport.web.dto.user.request.CreateUserRequest;
import ru.java.airport.web.dto.user.request.UpdateUserRequest;
import ru.java.airport.web.dto.user.response.CreateUserResponse;
import ru.java.airport.web.dto.user.response.GetUserResponse;
import ru.java.airport.web.dto.user.response.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @IsAuthenticated
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/me")
    @IsAuthenticated
    public ResponseEntity<UserResponse> updateCurrentUser(
        @Valid @RequestBody UpdateUserRequest request,
        Authentication authentication
    ) {
        Long userId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/me")
    @IsAuthenticated
    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @IsAdmin
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping
    @IsAdmin
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @IsAdmin
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/details")
    @IsAdmin
    public ResponseEntity<GetUserResponse> getUserWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithDetails(id));
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
