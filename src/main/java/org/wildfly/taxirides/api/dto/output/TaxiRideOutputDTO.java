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
public class TaxiRideOutputDTO {
    private Long id;
    private BigDecimal cost;
    private Integer duration;
    private LocalDateTime date;
    private SimpleDriverOutputDTO driver;
    private List<SimplePassengerOutputDTO> passengers;
}
