package org.wildfly.taxirides.api.dto.input;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaxiRideFilterInput {
    @QueryParam("startDate")
    private LocalDateTime startDate;

    @QueryParam("endDate")
    private LocalDateTime endDate;

    @QueryParam("minCost")
    private BigDecimal minCost;

    @QueryParam("maxCost")
    private BigDecimal maxCost;

    @QueryParam("minDuration")
    private Integer minDuration;

    @QueryParam("maxDuration")
    private Integer maxDuration;

    @QueryParam("driverId")
    private Long driverId;

    @QueryParam("passengerId")
    private Long passengerId;

    @QueryParam("passengerAge")
    private Integer passengerAge;
}
