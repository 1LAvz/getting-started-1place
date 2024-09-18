package org.wildfly.taxirides.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimpleDriverOutputDTO {
    private Long id;
    private String name;
}
