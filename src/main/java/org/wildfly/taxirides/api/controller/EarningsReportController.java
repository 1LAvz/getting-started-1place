package org.wildfly.taxirides.api.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wildfly.taxirides.api.dto.input.EarningsReportInput;
import org.wildfly.taxirides.api.dto.output.EarningsReportOutput;
import org.wildfly.taxirides.domain.service.EarningsReportService;
import java.util.List;

@Path("/earnings-report")
@Produces(MediaType.APPLICATION_JSON)
public class EarningsReportController {
    @Inject
    private EarningsReportService earningsReportService;

    @GET
    public Response getEarningsReport(@BeanParam @Valid EarningsReportInput input) {
        List<EarningsReportOutput> report = earningsReportService.reportEarnings(input);
        return Response.ok(report).build();
    }
}
