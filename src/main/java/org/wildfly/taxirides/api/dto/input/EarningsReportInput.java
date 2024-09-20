package org.wildfly.taxirides.api.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EarningsReportInput {
    @QueryParam("startDate")
    @NotNull(message = "Start date is required.")
    private LocalDateTime startDate;

    @QueryParam("endDate")
    @NotNull(message = "End date is required.")
    private LocalDateTime endDate;

    @QueryParam("driverId")
    private Long driverId;
}
