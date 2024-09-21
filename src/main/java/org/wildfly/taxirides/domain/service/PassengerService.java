package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.converter.PassengerConverter;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.exception.PassengerNotFoundException;
import org.wildfly.taxirides.domain.repository.PassengerRepository;
import java.util.List;

@ApplicationScoped
public class PassengerService {

    @Inject
    private PassengerRepository passengerRepository;

    @Inject
    private PassengerConverter passengerConverter;

    public List<PassengerOutput> listAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();

        return passengerConverter.convertToDTO(passengers);
    }

    public PassengerOutput addPassenger(PassengerInput passengerInput) {
        Passenger passenger = passengerConverter.convertToEntity(passengerInput);
        Passenger createdPassenger = passengerRepository.save(passenger);

        return passengerConverter.convertToDTO(createdPassenger);
    }

    public Passenger findOrFailPassengerBy(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId);
        if (passenger != null) {
            return passenger;
        }

        throw new PassengerNotFoundException(passengerId);
    }
}
