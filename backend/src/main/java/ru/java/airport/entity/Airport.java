package ru.java.airport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
@Getter
@Setter
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private String country;

    @Column(name = "iata_code")
    private String iataCode;

    @OneToMany(mappedBy = "departureAirport", fetch = FetchType.LAZY)
    private List<Flight> departingFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport", fetch = FetchType.LAZY)
    private List<Flight> arrivingFlights = new ArrayList<>();
}
