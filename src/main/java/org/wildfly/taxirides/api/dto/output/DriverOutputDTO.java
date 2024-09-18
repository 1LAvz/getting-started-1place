package org.wildfly.taxirides.api.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverOutputDTO {
    private Long id;
    private String licenceNumber;
    private String name;
}
