package org.wildfly.taxirides.domain.service;

import jakarta.inject.Inject;
import org.wildfly.taxirides.api.dto.input.PassengerInput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.exception.PassengerNotFoundException;
import org.wildfly.taxirides.domain.repository.PassengerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PassengerService {

    @Inject
    private PassengerRepository passengerRepository;

    public List<PassengerOutput> listAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();

        List<PassengerOutput> passengerDTOs = passengers.stream()
                .map(passenger -> PassengerOutput.builder()
                        .id(passenger.getId())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .age(passenger.getAge())
                        .build()
                ).collect(Collectors.toList());

        return passengerDTOs;
    }

    public PassengerOutput addPassenger(PassengerInput passengerInput) {
        Passenger passenger = Passenger.builder()
                .firstName(passengerInput.getFirstName())
                .lastName(passengerInput.getLastName())
                .age(passengerInput.getAge())
                .build();

        Passenger createdPassenger = passengerRepository.save(passenger);

        PassengerOutput passengerOutput = PassengerOutput.builder()
                .id(createdPassenger.getId())
                .firstName(createdPassenger.getFirstName())
                .lastName(createdPassenger.getLastName())
                .age(createdPassenger.getAge())
                .build();

        return passengerOutput;
    }

    public Passenger findOrFailPassengerBy(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId);
        if (passenger != null) {
            return passenger;
        }

        throw new PassengerNotFoundException(passengerId);
    }
}
