package org.wildfly.taxirides.dto;

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
