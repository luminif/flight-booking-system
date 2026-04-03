package ru.java.airport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.airport.constants.Constants;
import ru.java.airport.entity.Employee;
import ru.java.airport.entity.Role;
import ru.java.airport.entity.User;
import ru.java.airport.exception.EmployeeAlreadyExistsException;
import ru.java.airport.exception.EmployeeNotFoundException;
import ru.java.airport.exception.RoleNotFoundException;
import ru.java.airport.exception.UserNotFoundException;
import ru.java.airport.repository.EmployeeRepository;
import ru.java.airport.repository.RoleRepository;
import ru.java.airport.repository.UserRepository;
import ru.java.airport.web.dto.employee.request.CreateEmployeeRequest;
import ru.java.airport.web.dto.employee.request.UpdateEmployeeRequest;
import ru.java.airport.web.dto.employee.response.CrewAssignmentResponse;
import ru.java.airport.web.dto.employee.response.EmployeeResponse;
import ru.java.airport.web.dto.employee.response.GetEmployeeResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        if (user.getEmployee() != null) {
            throw new EmployeeAlreadyExistsException("User already has an employee profile");
        }

        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
            .orElseThrow(() -> new RoleNotFoundException(Constants.ROLE_NOT_FOUND_MESSAGE));

        if (!user.getRoles().contains(employeeRole)) {
            user.getRoles().add(employeeRole);
            userRepository.save(user);
        }

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setPosition(request.position());
        employee.setLicenseNumber(request.licenseNumber());

        Employee savedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(savedEmployee);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));

        if (request.firstName() != null) {
            employee.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            employee.setLastName(request.lastName());
        }
        if (request.position() != null) {
            employee.setPosition(request.position());
        }
        if (request.licenseNumber() != null) {
            employee.setLicenseNumber(request.licenseNumber());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(updatedEmployee);
    }

    public EmployeeResponse getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));
        return toEmployeeResponse(employee);
    }

    public GetEmployeeResponse getEmployeeWithCrewAssignments(Long id) {
        Employee employee = employeeRepository.findByIdWithCrewAssignments(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        List<CrewAssignmentResponse> assignments = employee.getCrewAssignments().stream()
            .map(assignment -> new CrewAssignmentResponse(
                assignment.getId(),
                assignment.getFlight().getId(),
                assignment.getFlight().getFlightNumber(),
                assignment.getRoleOnFlight()
            ))
            .collect(Collectors.toList());

        return new GetEmployeeResponse(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getPosition(),
            employee.getLicenseNumber(),
            employee.getUser().getId(),
            assignments
        );
    }

    public EmployeeResponse getEmployeeByUserId(Long userId) {
        Employee employee = employeeRepository.findByUserId(userId)
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));
        return toEmployeeResponse(employee);
    }

    public List<EmployeeResponse> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position).stream()
            .map(this::toEmployeeResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_MESSAGE));
        employeeRepository.delete(employee);
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(this::toEmployeeResponse)
            .collect(Collectors.toList());
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
            employee.getId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getPosition(),
            employee.getLicenseNumber(),
            employee.getUser().getId()
        );
    }
}
