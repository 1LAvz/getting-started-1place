package org.wildfly.taxirides.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverInput {
    @NotBlank
    @Size(max = 10)
    private String licenceNumber;

    @NotBlank
    @Size(max = 100)
    private String name;
}
