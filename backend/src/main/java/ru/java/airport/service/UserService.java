package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Role;
import ru.java.airport.entity.User;
import ru.java.airport.exception.RoleNotFoundException;
import ru.java.airport.exception.UserAlreadyExistsException;
import ru.java.airport.exception.UserNotFoundException;
import ru.java.airport.repository.RoleRepository;
import ru.java.airport.repository.UserRepository;
import ru.java.airport.web.dto.user.request.CreateUserRequest;
import ru.java.airport.web.dto.user.request.UpdateUserRequest;
import ru.java.airport.web.dto.user.response.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(Constants.USER_ALREADY_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role defaultRole = roleRepository.findByName("ROLE_PASSENGER")
            .orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND_MESSAGE));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return new CreateUserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getCreatedAt()
        );
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
        return toUserResponse(user);
    }

    public GetUserResponse getUserWithDetails(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        PassengerInfoResponse passengerInfo = null;
        if (user.getPassenger() != null) {
            passengerInfo = new PassengerInfoResponse(
                user.getPassenger().getId(),
                user.getPassenger().getFirstName(),
                user.getPassenger().getLastName(),
                user.getPassenger().getPassportNumber(),
                user.getPassenger().getPhone(),
                user.getPassenger().getEmail()
            );
        }

        EmployeeInfoResponse employeeInfo = null;
        if (user.getEmployee() != null) {
            employeeInfo = new EmployeeInfoResponse(
                user.getEmployee().getId(),
                user.getEmployee().getFirstName(),
                user.getEmployee().getLastName(),
                user.getEmployee().getPosition(),
                user.getEmployee().getLicenseNumber()
            );
        }

        return new GetUserResponse(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
            passengerInfo,
            employeeInfo
        );
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.roles() != null && !request.roles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : request.roles()) {
                Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND_MESSAGE));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
        userRepository.delete(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::toUserResponse)
            .collect(Collectors.toList());
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
