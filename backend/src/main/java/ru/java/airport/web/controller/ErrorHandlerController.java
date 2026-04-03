package ru.java.airport.web.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.java.airport.constants.Constants;
import ru.java.airport.exception.*;
import ru.java.airport.web.dto.error.ApiErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        log.error("User not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error("User already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleRoleNotFoundException(RoleNotFoundException e) {
        log.error("Role not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handlePassengerNotFoundException(PassengerNotFoundException e) {
        log.error("Passenger not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(PassengerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handlePassengerAlreadyExistsException(PassengerAlreadyExistsException e) {
        log.error("Passenger already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        log.error("Employee not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleAirlineNotFoundException(AirlineNotFoundException e) {
        log.error("Airline not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(AirlineAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleAirlineAlreadyExistsException(AirlineAlreadyExistsException e) {
        log.error("Airline already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(AirportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleAirportNotFoundException(AirportNotFoundException e) {
        log.error("Airport not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(AirportAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleAirportAlreadyExistsException(AirportAlreadyExistsException e) {
        log.error("Airport already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(AircraftNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleAircraftNotFoundException(AircraftNotFoundException e) {
        log.error("Aircraft not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(AircraftAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleAircraftAlreadyExistsException(AircraftAlreadyExistsException e) {
        log.error("Aircraft already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleFlightNotFoundException(FlightNotFoundException e) {
        log.error("Flight not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(FlightAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleFlightAlreadyExistsException(FlightAlreadyExistsException e) {
        log.error("Flight already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleTicketNotFoundException(TicketNotFoundException e) {
        log.error("Ticket not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(TicketAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleTicketAlreadyExistsException(TicketAlreadyExistsException e) {
        log.error("Ticket already exists: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(FlightCrewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleFlightCrewNotFoundException(FlightCrewNotFoundException e) {
        log.error("Flight crew assignment not found: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage());
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleAccessDeniedException() {
        log.error("Access denied");
        return new ApiErrorResponse(
            HttpStatus.FORBIDDEN,
            Constants.ACCESS_DENIED_MESSAGE
        );
    }

    @ExceptionHandler({ru.java.airport.exception.AccessDeniedException.class, ru.java.airport.exception.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleMyAccessDeniedException() {
        log.error("Access denied");
        return new ApiErrorResponse(
            HttpStatus.FORBIDDEN,
            Constants.ACCESS_DENIED_MESSAGE
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleAuthenticationException() {
        log.error("Authentication failed");
        return new ApiErrorResponse(
            HttpStatus.UNAUTHORIZED,
            Constants.AUTHENTICATION_FAILED_MESSAGE
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation failed: {}", e.getMessage());
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value",
                (msg1, msg2) -> msg1 + "; " + msg2
            ));
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            Constants.VALIDATION_FAILED_MESSAGE,
            errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage());
        Map<String, String> errors = e.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (msg1, msg2) -> msg1 + "; " + msg2
            ));
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            Constants.VALIDATION_FAILED_MESSAGE,
            errors
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGenericException(Exception e) {
        log.error("Unexpected error occurred", e);
        return new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getMessage()
        );
    }
}
