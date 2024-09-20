package org.wildfly.taxirides.api.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EarningsReportOutput {
    private Long driverId;
    private String driverName;
    private BigDecimal totalEarnings;
    private Long totalTaxiRides;
    private Double averageDuration;
    private Long totalPassengers;
    private Long totalPassengersUnder18;

    public EarningsReportOutput(Long driverId,
                                String driverName,
                                BigDecimal totalEarnings,
                                Long totalTaxiRides,
                                Double averageDuration,
                                Long totalPassengers) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.totalEarnings = totalEarnings;
        this.totalTaxiRides = totalTaxiRides;
        this.averageDuration = averageDuration;
        this.totalPassengers = totalPassengers;
    }
}
