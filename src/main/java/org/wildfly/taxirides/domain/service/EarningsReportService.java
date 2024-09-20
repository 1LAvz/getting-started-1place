package org.wildfly.taxirides.domain.service;

import org.wildfly.taxirides.api.dto.input.EarningsReportInput;
import org.wildfly.taxirides.api.dto.output.EarningsReportOutput;
import java.util.List;

public interface EarningsReportService {
    List<EarningsReportOutput> reportEarnings(EarningsReportInput input);
}
