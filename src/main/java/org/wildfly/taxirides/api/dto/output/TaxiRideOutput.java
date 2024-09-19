package org.wildfly.taxirides.api.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class TaxiRideOutput {
    private Long id;
    private BigDecimal cost;
    private Integer duration;
    private LocalDateTime date;
    private DriverOutput driver;
    private List<PassengerOutput> passengers;
}
