INSERT INTO Driver (name, licenseNumber) VALUES ('John Doe', 'ABC123');
INSERT INTO Driver (name, licenseNumber) VALUES ('Maria Doe', 'CDE421');
INSERT INTO Driver (name, licenseNumber) VALUES ('John WICK', 'SAS312');

INSERT INTO Passenger (firstName, lastName, age) VALUES ('Jane', 'Smith', 19);
INSERT INTO Passenger (firstName, lastName, age) VALUES ('Pedro', 'Sampaio', 17);
INSERT INTO Passenger (firstName, lastName, age) VALUES ('Lucas', 'Avanzi', 27);
INSERT INTO Passenger (firstName, lastName, age) VALUES ('Joao', 'Vitor', 19);
INSERT INTO Passenger (firstName, lastName, age) VALUES ('Felipe', 'Salles', 15);


INSERT INTO TaxiRide (cost, duration, date, driver_id) VALUES (30, 20, '2023-10-17T14:30:00', 1);
INSERT INTO TaxiRide (cost, duration, date, driver_id) VALUES (25, 10, '2023-10-18T15:30:00', 2);
INSERT INTO TaxiRide (cost, duration, date, driver_id) VALUES (37, 25, '2023-10-25T19:30:00', 2);
INSERT INTO TaxiRide (cost, duration, date, driver_id) VALUES (13, 7, '2023-10-26T19:30:00', 2);


INSERT INTO taxi_ride_passenger (taxi_ride_id, passenger_id) VALUES (1, 1), (1, 2);
INSERT INTO taxi_ride_passenger (taxi_ride_id, passenger_id) VALUES (2, 2), (2, 3);
INSERT INTO taxi_ride_passenger (taxi_ride_id, passenger_id) VALUES (3, 4);
INSERT INTO taxi_ride_passenger (taxi_ride_id, passenger_id) VALUES (4, 2), (4, 5);

