package org.wildfly.taxirides.api.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningsReportOutput {
    private String driverName;
    private BigDecimal totalEarnings;
    private Double averageDuration;
    private Long under18PassengersCount;
    private Long totalPassengersCount;
    private Long totalRidesCount;
}
