package org.wildfly.taxirides.domain.converter;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.entity.TaxiRide;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TaxiRideConverter {

    @Inject
    private ModelMapper modelMapper;

    public TaxiRideOutput convertToDTO(TaxiRide taxiride) {
        return modelMapper.map(taxiride, TaxiRideOutput.class);
    }

    public List<TaxiRideOutput> convertToDTO(List<TaxiRide> taxiride) {
        return taxiride.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
