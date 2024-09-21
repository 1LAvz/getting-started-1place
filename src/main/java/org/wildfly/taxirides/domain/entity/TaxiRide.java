package org.wildfly.taxirides.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQuery(name = "TaxiRide.findById", query = "SELECT t FROM TaxiRide t WHERE t.id = :id")
public class TaxiRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private BigDecimal cost;

    @NotNull
    private Integer duration; // duration in minutes

    @NotNull
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "taxi_ride_passenger",
            joinColumns = @JoinColumn(name = "taxi_ride_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id"))
    private List<Passenger> passengers = new ArrayList<>();

    public boolean hasPassenger(Passenger passenger) {
        return passengers.contains(passenger);
    }
}
