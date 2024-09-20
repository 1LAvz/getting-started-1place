package org.wildfly.taxirides.api.dto.output;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotNull
public class DriverPassengerCount {
    private Long driverId;
    private Long totalPassengers;
}
