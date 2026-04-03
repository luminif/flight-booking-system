package ru.java.airport.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import ru.java.airport.entity.Aircraft;
import ru.java.airport.security.JwtEntity;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Utils {
    public static Long getCurrentUserId(Authentication authentication) {
        JwtEntity principal = (JwtEntity) authentication.getPrincipal();
        return principal.getId();
    }

    public static List<String> generateAllSeats(Aircraft aircraft) {
        List<String> seats = new ArrayList<>();

        Integer seatsPerRow = aircraft.getSeatsPerRow();
        String[] seatLetters = aircraft.getSeatLetters().split(",");
        Integer totalSeats = aircraft.getCapacity();

        int rows = totalSeats / seatsPerRow;

        for (int row = 1; row <= rows + 1 && seats.size() < totalSeats; row++) {
            for (String letter : seatLetters) {
                if (seats.size() < totalSeats) {
                    seats.add(row + letter);
                }
            }
        }

        return seats;
    }

    public static boolean isValidSeat(Aircraft aircraft, String seatNumber) {
        return generateAllSeats(aircraft).contains(seatNumber);
    }
}
