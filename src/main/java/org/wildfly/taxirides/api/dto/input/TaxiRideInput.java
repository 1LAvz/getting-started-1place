package org.wildfly.taxirides.api.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaxiRideInput {
    @NotNull
    private BigDecimal cost;

    @NotNull
    private Integer duration;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Long driverId;

    @NotNull
    private List<Long> passengerIds;
}
