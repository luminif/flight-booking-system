package ru.java.airport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aircrafts")
@Getter
@Setter
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private Integer capacity;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

    @Column(name = "seat_letters")
    private String seatLetters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @Column(name = "registration_number")
    private String registrationNumber;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> flights = new ArrayList<>();
}
