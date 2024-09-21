package org.wildfly.taxirides.domain.converter;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.entity.Passenger;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PassengerConverter {

    @Inject
    private ModelMapper modelMapper;

    public PassengerOutput convertToDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerOutput.class);
    }

    public List<PassengerOutput> convertToDTO(List<Passenger> passenger) {
        return passenger.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Passenger convertToEntity(PassengerInput passengerInput) {
        return modelMapper.map(passengerInput, Passenger.class);
    }
}
