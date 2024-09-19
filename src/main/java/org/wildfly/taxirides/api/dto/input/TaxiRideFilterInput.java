package org.wildfly.taxirides.api.dto.input;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaxiRideFilterInput {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal minCost;
    private BigDecimal maxCost;
    private Integer minDuration;
    private Integer maxDuration;
    private Long driverId;
    private Long passengerId;
    private Integer passengerAge;
}
