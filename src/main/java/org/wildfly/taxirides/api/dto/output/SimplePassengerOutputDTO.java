package org.wildfly.taxirides.api.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimplePassengerOutputDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
