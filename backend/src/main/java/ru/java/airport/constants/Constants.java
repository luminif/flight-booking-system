package ru.java.airport.constants;

public final class Constants {
    private Constants() {

    }

    public final static String UNAUTHORIZED = "Unauthorized";

    public final static String AUTHORIZATION = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public static final String USER_ALREADY_EXISTS_MESSAGE = "User with this username already exists";

    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found";

    public static final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger not found";

    public static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Employee not found";

    public static final String AIRLINE_NOT_FOUND_MESSAGE = "Airline not found";

    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    public static final String AIRCRAFT_NOT_FOUND_MESSAGE = "Aircraft not found";

    public static final String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";

    public static final String TICKET_NOT_FOUND_MESSAGE = "Ticket not found";

    public static final String FLIGHT_ALREADY_STARTED_MESSAGE = "Cannot cancel ticket after flight has started";

    public static final String FLIGHT_CREW_NOT_FOUND_MESSAGE = "Flight crew assignment not found";

    public static final String ACCESS_DENIED_MESSAGE = "Access denied";

    public static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    public static final String AUTHENTICATION_FAILED_MESSAGE = "Authentication failed";
}
