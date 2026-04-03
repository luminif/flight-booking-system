package ru.java.airport.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.java.airport.annotations.IsAdmin;
import ru.java.airport.annotations.IsEmployeeOrAdmin;
import ru.java.airport.service.EmployeeService;
import ru.java.airport.utils.Utils;
import ru.java.airport.web.dto.employee.request.CreateEmployeeRequest;
import ru.java.airport.web.dto.employee.request.UpdateEmployeeRequest;
import ru.java.airport.web.dto.employee.response.EmployeeResponse;
import ru.java.airport.web.dto.employee.response.GetEmployeeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/me")
    @IsEmployeeOrAdmin
    public ResponseEntity<EmployeeResponse> getCurrentEmployee(Authentication authentication) {
        Long userId = Utils.getCurrentUserId(authentication);
        return ResponseEntity.ok(employeeService.getEmployeeByUserId(userId));
    }

    @PutMapping("/me")
    @IsEmployeeOrAdmin
    public ResponseEntity<EmployeeResponse> updateCurrentEmployee(
        @Valid @RequestBody UpdateEmployeeRequest request,
        Authentication authentication
    ) {
        Long userId = Utils.getCurrentUserId(authentication);
        EmployeeResponse employee = employeeService.getEmployeeByUserId(userId);
        return ResponseEntity.ok(employeeService.updateEmployee(employee.id(), request));
    }

    @PostMapping("/user/{userId}")
    @IsAdmin
    public ResponseEntity<EmployeeResponse> createEmployee(
        @PathVariable Long userId,
        @Valid @RequestBody CreateEmployeeRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(employeeService.createEmployee(request, userId));
    }

    @GetMapping
    @IsEmployeeOrAdmin
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @IsEmployeeOrAdmin
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @GetMapping("/{id}/crew")
    @IsEmployeeOrAdmin
    public ResponseEntity<GetEmployeeResponse> getEmployeeWithCrewAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeWithCrewAssignments(id));
    }

    @GetMapping("/user/{userId}")
    @IsEmployeeOrAdmin
    public ResponseEntity<EmployeeResponse> getEmployeeByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(employeeService.getEmployeeByUserId(userId));
    }

    @GetMapping("/search")
    @IsEmployeeOrAdmin
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByPosition(
        @RequestParam String position
    ) {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(position));
    }

    @PutMapping("/{id}")
    @IsAdmin
    public ResponseEntity<EmployeeResponse> updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody UpdateEmployeeRequest request
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}