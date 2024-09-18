package org.wildfly.taxirides.services;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.entities.Passenger;
import org.wildfly.taxirides.repositories.PassengerRepository;

import java.util.List;

@Transactional
public class PassengerService {

    @Inject
    private PassengerRepository passengerRepository;

    public List<Passenger> listAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger findPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    public Passenger addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id);
        if (passenger != null) {
            passengerRepository.delete(passenger);
        }
    }

    public Passenger updatePassenger(Long id, Passenger passengerDetails) {
        Passenger passenger = passengerRepository.findById(id);
        if (passenger != null) {
            passenger.setFirstName(passengerDetails.getFirstName());
            passenger.setLastName(passengerDetails.getLastName());
            passenger.setAge(passengerDetails.getAge());
            return passengerRepository.save(passenger);
        }
        return null;
    }
}
