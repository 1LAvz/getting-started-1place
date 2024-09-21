package org.wildfly.taxirides.api.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxiRideInput {
    @NotNull
    @PositiveOrZero
    private BigDecimal cost;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Long driverId;

    @NotNull
    private Set<Long> passengerIds;
}
