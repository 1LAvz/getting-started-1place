package org.wildfly.taxirides.domain.converter;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.wildfly.taxirides.api.dto.input.DriverInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.domain.entity.Driver;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DriverConverter {

    @Inject
    private ModelMapper modelMapper;

    public DriverOutput convertToDTO(Driver driver) {
        return modelMapper.map(driver, DriverOutput.class);
    }

    public List<DriverOutput> convertToDTO(List<Driver> driver) {
        return driver.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Driver convertToEntity(DriverInput driverInput) {
        return modelMapper.map(driverInput, Driver.class);
    }
}
