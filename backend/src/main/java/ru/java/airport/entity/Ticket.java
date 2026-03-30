package ru.java.airport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "purchase_date")
    @CreationTimestamp
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.CONFIRMED;
}
