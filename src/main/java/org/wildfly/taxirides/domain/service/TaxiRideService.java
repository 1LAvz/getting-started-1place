package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.wildfly.taxirides.api.dto.input.TaxiRideInput;
import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.converter.TaxiRideConverter;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.exception.BusinessException;
import org.wildfly.taxirides.domain.exception.PassengerNotFoundException;
import org.wildfly.taxirides.domain.exception.TaxiRideNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;
import org.wildfly.taxirides.domain.repository.PassengerRepository;
import org.wildfly.taxirides.domain.repository.TaxiRideRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaxiRideService {

    @Inject
    private TaxiRideRepository taxiRideRepository;

    @Inject
    private DriverRepository driverRepository;

    @Inject
    private PassengerRepository passengerRepository;

    @Inject
    private PassengerService passengerService;

    @Inject
    private DriverService driverService;

    @Inject
    private TaxiRideConverter taxiRideConverter;

    public TaxiRideOutput addTaxiRide(TaxiRideInput input) {
        TaxiRide taxiRide = validateAndCreateTaxiRide(input);
        return taxiRideConverter.convertToDTO(taxiRideRepository.save(taxiRide));
    }

    public TaxiRideOutput updateTaxiRide(Long taxiRideId, TaxiRideInput taxiRideinput) {
        findOrFailTaxiRideBy(taxiRideId);
        TaxiRide taxiRide = validateAndCreateTaxiRide(taxiRideinput);
        taxiRide.setId(taxiRideId);

        return taxiRideConverter.convertToDTO(taxiRideRepository.save(taxiRide));
    }

    public List<TaxiRideOutput> findTaxiRides(TaxiRideFilterInput input) {
        List<TaxiRide> taxiRides = taxiRideRepository.findTaxiRides(input);

        return taxiRideConverter.convertToDTO(taxiRides);
    }

    public TaxiRide findOrFailTaxiRideBy(Long taxiRideId) {
        TaxiRide taxiRide = taxiRideRepository.findById(taxiRideId);
        if (taxiRide != null) {
            return taxiRide;
        }

        throw new TaxiRideNotFoundException(taxiRideId);
    }

    private List<Passenger> findPassengers(Set<Long> passengersIds) {
        List<Passenger> passengers = passengerRepository.findByIds(passengersIds);
        validatePassengers(passengers, passengersIds);

        return passengers;
    }

    public void deleteTaxiRide(Long taxiRideId) {
        TaxiRide taxiRide = findOrFailTaxiRideBy(taxiRideId);
        taxiRideRepository.delete(taxiRide);
    }

    public void deletePassengerFromTaxiRide(Long taxiRideId, Long passengerId) {
        TaxiRide taxiRide = findOrFailTaxiRideBy(taxiRideId);
        Passenger passenger = passengerService.findOrFailPassengerBy(passengerId);

        if (!taxiRide.hasPassenger(passenger)) {
            throw new BusinessException(String.format("Passenger with id %d is not part of the taxi ride.", passengerId));
        } else if (taxiRide.isPassengerTheOnlyOne(passenger)) {
            throw new BusinessException("Is not possible to remove the only passenger in the taxi ride.");
        }

        taxiRide.getPassengers().remove(passenger);
        taxiRideRepository.save(taxiRide);
    }

    private TaxiRide validateAndCreateTaxiRide(TaxiRideInput input) {
        Driver driver = driverService.findOrFailDriverBy(input.getDriverId());
        List<Passenger> passengers = findPassengers(input.getPassengerIds());

        return TaxiRide.builder()
                .cost(input.getCost())
                .duration(input.getDuration())
                .date(input.getDate())
                .driver(driver)
                .passengers(passengers)
                .build();
    }

    public void validatePassengers(List<Passenger> passengersFound, Set<Long> inputtedPassengersIds) {
        if (inputtedPassengersIds.isEmpty()) {
            throw new BusinessException("At least one passenger is required");
        }

        checkIfAllPassengersInformedDoExists(passengersFound, inputtedPassengersIds);
    }

    public void checkIfAllPassengersInformedDoExists(List<Passenger> passengersFound, Set<Long> inputtedPassengersIds) {
        Set<Long> foundPassengerIds = passengersFound.stream()
                .map(Passenger::getId)
                .collect(Collectors.toSet());

        Set<Long> inputPassengerIds = new HashSet<>(inputtedPassengersIds);
        inputPassengerIds.removeAll(foundPassengerIds);

        if (!inputPassengerIds.isEmpty()) {
            throw new PassengerNotFoundException("Passengers not found for IDs: " + inputPassengerIds);
        }
    }
}
