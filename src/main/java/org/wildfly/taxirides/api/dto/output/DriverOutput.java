package org.wildfly.taxirides.api.dto.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverOutput {
    private Long id;
    private String name;
    private String licenseNumber;
}
