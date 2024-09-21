package org.wildfly.taxirides.domain.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.wildfly.taxirides.api.dto.input.TaxiRideInput;
import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.api.dto.output.DriverOutput;
import org.wildfly.taxirides.api.dto.output.PassengerOutput;
import org.wildfly.taxirides.api.dto.output.TaxiRideOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.exception.BusinessException;
import org.wildfly.taxirides.domain.exception.TaxiRideNotFoundException;
import org.wildfly.taxirides.domain.repository.DriverRepository;
import org.wildfly.taxirides.domain.repository.PassengerRepository;
import org.wildfly.taxirides.domain.repository.TaxiRideRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
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

    public TaxiRideOutput addTaxiRide(TaxiRideInput input) {

        Driver driver = driverService.findOrFailDriverBy(input.getDriverId());

        List<Passenger> passengers = findPassengers(input.getPassengerIds());

        TaxiRide taxiRide = TaxiRide.builder()
                .cost(input.getCost())
                .duration(input.getDuration())
                .date(input.getDate())
                .driver(driver)
                .passengers(passengers)
                .build();
        taxiRide.setDriver(driver);
        taxiRide.setPassengers(passengers);

        TaxiRide createdTaxiRide = taxiRideRepository.save(taxiRide);


        DriverOutput driverOutput = DriverOutput.builder()
                .id(createdTaxiRide.getDriver().getId())
                .name(createdTaxiRide.getDriver().getName())
                .licenseNumber(createdTaxiRide.getDriver().getLicenseNumber())
                .build();

        List<PassengerOutput> passengersOutput = createdTaxiRide.getPassengers().stream()
                .map(passenger -> PassengerOutput.builder()
                        .id(passenger.getId())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .age(passenger.getAge())
                        .build())
                .collect(Collectors.toList());

        TaxiRideOutput taxiRideOutput = TaxiRideOutput.builder()
                .id(createdTaxiRide.getId())
                .cost(createdTaxiRide.getCost())
                .duration(createdTaxiRide.getDuration())
                .date(createdTaxiRide.getDate())
                .driver(driverOutput)
                .passengers(passengersOutput)
                .build();

        return taxiRideOutput;
    }

    private List<Passenger> findPassengers(Set<Long> passengersIds) {
        List<Passenger> passengers = passengerRepository.findByIds(passengersIds);
        validatePassengers(passengers, passengersIds);

        return passengers;
    }

    public void validatePassengers(List<Passenger> foundPassengers, Set<Long> inputedPassengersIds)  {
        if (foundPassengers.isEmpty()) {
            throw new RuntimeException("At least one passenger is required");
        }

        Set<Long> foundPassengerIds = foundPassengers.stream()
                .map(Passenger::getId)
                .collect(Collectors.toSet());

        Set<Long> inputPassengerIds = new HashSet<>(inputedPassengersIds);
        inputPassengerIds.removeAll(foundPassengerIds);

        if (!inputPassengerIds.isEmpty()) {
            throw new EntityNotFoundException("Passengers not found for IDs: " + inputPassengerIds);
        }
    }

    public TaxiRideOutput updateTaxiRide(Long taxiRideId, TaxiRideInput taxiRideinput) {
        TaxiRide taxiRide = findOrFailTaxiRideBy(taxiRideId);

        taxiRide.setCost(taxiRideinput.getCost());
        taxiRide.setDuration(taxiRideinput.getDuration());
        taxiRide.setDate(taxiRideinput.getDate());

        Driver driver = driverRepository.findById(taxiRideinput.getDriverId());
        taxiRide.setDriver(driver);

        List<Passenger> passengers = passengerRepository.findByIds(taxiRideinput.getPassengerIds());
        validatePassengers(passengers, taxiRideinput.getPassengerIds());
        taxiRide.setPassengers(passengers);

        TaxiRide updatedTaxiRide = taxiRideRepository.save(taxiRide);

        DriverOutput driverOutput = DriverOutput.builder()
                .id(updatedTaxiRide.getDriver().getId())
                .name(updatedTaxiRide.getDriver().getName())
                .licenseNumber(updatedTaxiRide.getDriver().getLicenseNumber())
                .build();

        List<PassengerOutput> passengersOutput = updatedTaxiRide.getPassengers().stream()
                .map(passenger -> PassengerOutput.builder()
                        .id(passenger.getId())
                        .firstName(passenger.getFirstName())
                        .lastName(passenger.getLastName())
                        .age(passenger.getAge())
                        .build())
                .collect(Collectors.toList());

        TaxiRideOutput taxiRideOutput = TaxiRideOutput.builder()
                .id(updatedTaxiRide.getId())
                .cost(updatedTaxiRide.getCost())
                .duration(updatedTaxiRide.getDuration())
                .date(updatedTaxiRide.getDate())
                .driver(driverOutput)
                .passengers(passengersOutput)
                .build();

        return  taxiRideOutput;
    }

    public void deletePassengerFromTaxiRide(Long taxiRideId, Long passengerId) {
        TaxiRide taxiRide = findOrFailTaxiRideBy(taxiRideId);
        Passenger passenger = passengerService.findOrFailPassengerBy(passengerId);

        boolean isPassengerInRide = taxiRide.hasPassenger(passenger);

        if(!isPassengerInRide) {
            throw new BusinessException(String.format("Passenger with id %d is not part of the taxi ride.", passengerId));
        }

        taxiRide.getPassengers().remove(passenger);
        taxiRideRepository.save(taxiRide);
    }

    public void deleteTaxiRide(Long taxiRideId) {
        TaxiRide taxiRide = findOrFailTaxiRideBy(taxiRideId);
        taxiRideRepository.delete(taxiRide);
    }

    public TaxiRide findOrFailTaxiRideBy(Long taxiRideId) {
        TaxiRide taxiRide = taxiRideRepository.findById(taxiRideId);
        if (taxiRide != null) {
            return taxiRide;
        }

        throw new TaxiRideNotFoundException(taxiRideId);
    }

    public List<TaxiRideOutput> findTaxiRides(TaxiRideFilterInput input) {

        List<TaxiRide> taxiRides = taxiRideRepository.findTaxiRides(input);

        return taxiRides.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TaxiRideOutput convertToDTO(TaxiRide taxiRide) {
        return TaxiRideOutput.builder()
                .id(taxiRide.getId())
                .cost(taxiRide.getCost())
                .duration(taxiRide.getDuration())
                .date(taxiRide.getDate())
                .driver(convertToDriverOutput(taxiRide.getDriver()))
                .passengers(convertToPassengerOutputList(taxiRide.getPassengers()))
                .build();
    }

    private DriverOutput convertToDriverOutput(Driver driver) {
        return DriverOutput.builder()
                .id(driver.getId())
                .name(driver.getName())
                .licenseNumber(driver.getLicenseNumber())
                .build();
    }

    private List<PassengerOutput> convertToPassengerOutputList(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::convertToPassengerOutput)
                .collect(Collectors.toList());
    }

    private PassengerOutput convertToPassengerOutput(Passenger passenger) {
        return PassengerOutput.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .age(passenger.getAge())
                .build();
    }
}
