package org.wildfly.taxirides.domain.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.exception.PassengerNotFoundException;
import org.wildfly.taxirides.domain.exception.TaxiRideNotFoundException;
import org.wildfly.taxirides.domain.repository.intarface.PassengerRepository;

import java.util.List;

@Transactional
public class PassengerService {

    @Inject
    private PassengerRepository passengerRepository;

    public List<Passenger> listAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Passenger findOrFailPassengerBy(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId);
        if (passenger != null) {
            return passenger;
        }

        throw new PassengerNotFoundException(passengerId);
    }
}
