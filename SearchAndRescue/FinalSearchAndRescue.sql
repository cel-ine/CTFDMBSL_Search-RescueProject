CREATE DATABASE FinalSearchAndRescue
USE FinalSearchAndRescue

CREATE TABLE Barangay (
    barangayID INT PRIMARY KEY AUTO_INCREMENT,
    barangayName VARCHAR(255) NOT NULL,
    barangayRescueCount INT DEFAULT 0,
    barangayDistance DOUBLE NOT NULL,
    barangayLongitude DOUBLE DEFAULT NULL,
    barangayLatitude DOUBLE DEFAULT NULL
);

INSERT INTO Barangay (barangayName, barangayRescueCount, barangayDistance, barangayLongitude, barangayLatitude)
VALUES
('Dela Paz', 0, 6.4, NULL, NULL),
('Santolan', 0, 5.8, NULL, NULL),
('San Antonio', 0, 4.9, NULL, NULL),
('Manggahan', 0, 4.7, NULL, NULL),
('Santa Lucia', 0, 4.0, NULL, NULL),
('Pinagbuhatan', 0, 4.0, NULL, NULL),
('San Joaquin', 0, 3.7, NULL, NULL),
('Kapitolyo', 0, 3.6, NULL, NULL),
('Rosario', 0, 3.9, NULL, NULL),
('Kalawaan', 0, 3.8, NULL, NULL),
('Ugong', 0, 3.3, NULL, NULL),
('Oranbo', 0, 3.2, NULL, NULL),
('Buting', 0, 3.0, NULL, NULL),
('Pineda', 0, 3.0, NULL, NULL),
('Malinao', 0, 2.6, NULL, NULL),
('Bambang', 0, 2.5, NULL, NULL),
('Sumilang', 0, 2.4, NULL, NULL),
('Santa Rosa', 0, 2.4, NULL, NULL),
('Santo Tomas', 0, 2.0, NULL, NULL),
('Bagong Katipunan', 0, 1.8, NULL, NULL),
('Palatiw', 0, 1.8, NULL, NULL),
('Bagong Ilog', 0, 1.7, NULL, NULL),
('San Jose', 0, 1.7, NULL, NULL),
('Santa Cruz', 0, 1.7, NULL, NULL),
('Maybunga', 0, 1.4, NULL, NULL),
('Caniogan', 0, 1.4, NULL, NULL),
('Kapasigan', 0, 1.3, NULL, NULL),
('San Miguel', 0, 1.3, NULL, NULL),
('Sagad', 0, 1.3, NULL, NULL),
('San Nicolas', 0, 1.2, NULL, NULL);

CREATE TABLE BarangayDescription (
    descriptionID INT PRIMARY KEY AUTO_INCREMENT,
    barangayCaptain VARCHAR (100),
    barangayHazardProne VARCHAR(255),
    barangayPopulation INT,
    barangayID INT,
    FOREIGN KEY (barangayID) REFERENCES Barangay(barangayID) ON DELETE CASCADE
);

INSERT INTO BarangayDescription (barangayCaptain, barangayHazardProne, barangayPopulation, barangayID)
VALUES
('Dietmar L. Romualdez', 'Flood-prone', 19804, (SELECT barangayID FROM Barangay WHERE barangayName = 'Dela Paz')),
('Jose Gabriel G. Bayan', 'Flood-prone', 57933, (SELECT barangayID FROM Barangay WHERE barangayName = 'Santolan')),
('Thomas Raymond U. Lising', 'Earthquake-prone', 11727, (SELECT barangayID FROM Barangay WHERE barangayName = 'San Antonio')),
('Quin A. Cruz', 'Flood-prone', 88078, (SELECT barangayID FROM Barangay WHERE barangayName = 'Manggahan')),
('Aldwin M. Ilagan', 'Flood/Earthquake-prone', 43749, (SELECT barangayID FROM Barangay WHERE barangayName = 'Santa Lucia')),
('Robin R. Salandanan', 'Flood/Earthquake-prone', 163598, (SELECT barangayID FROM Barangay WHERE barangayName = 'Pinagbuhatan')),
('Franz Julius B. Sanchez', 'Earthquake-prone', 13823, (SELECT barangayID FROM Barangay WHERE barangayName = 'San Joaquin')),
('Alexis Rafael M. Torres', 'Earthquake/Fire-prone', 9203, (SELECT barangayID FROM Barangay WHERE barangayName = 'Kapitolyo')),
('Aquilino S. Dela Cruz Sr', 'Flood-prone', 73979, (SELECT barangayID FROM Barangay WHERE barangayName = 'Rosario')),
('Jessie B. Gaviola', 'Flood-prone', 32145, (SELECT barangayID FROM Barangay WHERE barangayName = 'Kalawaan')),
('Gloria P. Cruz', 'Fire-prone', 28737, (SELECT barangayID FROM Barangay WHERE barangayName = 'Ugong')),
('Richard M. Pua', 'Flood-prone', 3267, (SELECT barangayID FROM Barangay WHERE barangayName = 'Oranbo')),
('Marlon S. Servillon', 'Flood-prone', 10348, (SELECT barangayID FROM Barangay WHERE barangayName = 'Buting')),
('Francisco D. De Leon', 'Flood-prone', 19499, (SELECT barangayID FROM Barangay WHERE barangayName = 'Pineda')),
('Joel S. Dela Cruz', 'Flood/Earthquake-prone', 4817, (SELECT barangayID FROM Barangay WHERE barangayName = 'Malinao')),
('Rodel A. Samandiego', 'Flood-prone', 20801, (SELECT barangayID FROM Barangay WHERE barangayName = 'Bambang')),
('Irma R. Gomez', 'Earthquake/Landslide-prone', 4334, (SELECT barangayID FROM Barangay WHERE barangayName = 'Sumilang')),
('Arcel T. Umipig', 'Earthquake/Landslide-prone', 1015, (SELECT barangayID FROM Barangay WHERE barangayName = 'Santa Rosa')),
('Amelia L. Raymundo', 'Flood-prone', 12904, (SELECT barangayID FROM Barangay WHERE barangayName = 'Santo Tomas')),
('Jeronimo R. Alba', 'Fire-prone', 879, (SELECT barangayID FROM Barangay WHERE barangayName = 'Bagong Katipunan')),
('Eriberto M. Guevarra', 'Flood-prone', 27499, (SELECT barangayID FROM Barangay WHERE barangayName = 'Palatiw')),
('Ferdinand A. Avis', 'Flood-prone', 20344, (SELECT barangayID FROM Barangay WHERE barangayName = 'Bagong Ilog')),
('Noel A. Dela Cruz', 'Flood-prone', 1814, (SELECT barangayID FROM Barangay WHERE barangayName = 'San Jose')),
('Roderick Mario U. Gonzales', 'Flood-prone', 5610, (SELECT barangayID FROM Barangay WHERE barangayName = 'Santa Cruz')),
('Arnold R. Alvarez', 'Flood-prone', 45555, (SELECT barangayID FROM Barangay WHERE barangayName = 'Maybunga')),
('Petri S. Cortez', 'Flood-prone', 28086, (SELECT barangayID FROM Barangay WHERE barangayName = 'Caniogan')),
('Jenifer R. Marcelo', 'Earthquake-prone', 4774, (SELECT barangayID FROM Barangay WHERE barangayName = 'Kapasigan')),
('Manuel E. Alba', 'Flood-prone', 40199, (SELECT barangayID FROM Barangay WHERE barangayName = 'San Miguel')),
('Marvin D. Benito', 'Flood-prone', 6036, (SELECT barangayID FROM Barangay WHERE barangayName = 'Sagad')),
('Rigor J. Enriquez', 'Flood-prone', 2602, (SELECT barangayID FROM Barangay WHERE barangayName = 'San Nicolas'));

SELECT 
    Barangay.barangayName,
    BarangayDescription.barangayCaptain,
    BarangayDescription.barangayHazardProne,
    BarangayDescription.barangayPopulation
FROM 
    Barangay
JOIN 
    BarangayDescription ON Barangay.barangayID = BarangayDescription.barangayID;
    
CREATE TABLE AdminAccount (
	accountID INT PRIMARY KEY,
    username VARCHAR(50),
    adminPin INT
);

INSERT INTO AdminAccount (accountID, username, adminPin) VALUES
(1, 'mainAdmin', 1234),
(2, 'adminPasig', 4321),
(3, 'supervisor', 1111),
(4, 'rescueLead', 5678);

CREATE TABLE Emergency (
    incidentNumber VARCHAR(80) PRIMARY KEY,
    barangayID INT,
    emergencyType ENUM('Flood', 'Fire', 'Earthquake', 'Landslide'),
    emergencySeverity ENUM('Low', 'Moderate', 'High', 'Critical', 'Extreme'),
    emergencyRescueCount INT,
    emergencyStatus ENUM('ENROUTE', 'ON SCENE', 'QUEUED', 'DISPATCHED'),
    dateIssued DATE,
    peopleID INT,
    FOREIGN KEY (barangayID) REFERENCES Barangay(barangayID),
    FOREIGN KEY (peopleID) REFERENCES PeopleCount(peopleID)
);

CREATE TABLE History ( -- removed the dispatch date + time
    historyID INT PRIMARY KEY AUTO_INCREMENT,
    incidentNumber VARCHAR(80),  
    barangayID INT,
    dispatchTimestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incidentNumber) REFERENCES Emergency(incidentNumber) ON DELETE CASCADE,
    FOREIGN KEY (barangayID) REFERENCES Barangay(barangayID) ON DELETE CASCADE
);

CREATE TABLE PeopleCount (
    peopleID INT PRIMARY KEY AUTO_INCREMENT,
    peopleMemberCount INT,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    numOfChildren INT,
    numOfAdults INT,
    numOfSeniors INT
);

-- Update the peopleMemberCount as the sum of numOfChildren, numOfAdults, and numOfSeniors
UPDATE PeopleCount
SET peopleMemberCount = COALESCE(numOfChildren, 0) + COALESCE(numOfAdults, 0) + COALESCE(numOfSeniors, 0);

CREATE TABLE EmergencyReport (
    reportID INT PRIMARY KEY AUTO_INCREMENT,
    historyID INT,
    reportWriter VARCHAR(255), -- name
    reportRemarks VARCHAR(255), -- 
    emergencyActionTaken VARCHAR(255),
    FOREIGN KEY (historyID) REFERENCES History(historyID)
);

SELECT * FROM EmergencyReport

SELECT e.incidentNumber, e.emergencyType, e.emergencyStatus, 
                   e.dateIssued, b.barangayName, 
                   p.firstName, p.lastName,
                   (p.numOfChildren + p.numOfAdults + p.numOfSeniors) AS totalRescuees,
                   e.emergencySeverity  -- Fetch the emergencySeverity directly from the database
            FROM Emergency e
            JOIN PeopleCount p ON e.peopleID = p.peopleID
            JOIN Barangay b ON e.barangayID = b.barangayID
            ORDER BY e.dateIssued DESC
            
DELIMITER //

CREATE TRIGGER trg_people_insert
BEFORE INSERT ON PeopleCount
FOR EACH ROW
BEGIN
  SET NEW.peopleMemberCount = COALESCE(NEW.numOfChildren, 0) + COALESCE(NEW.numOfAdults, 0) + COALESCE(NEW.numOfSeniors, 0);
END;
//

CREATE TRIGGER trg_people_update
BEFORE UPDATE ON PeopleCount
FOR EACH ROW
BEGIN
  SET NEW.peopleMemberCount = COALESCE(NEW.numOfChildren, 0) + COALESCE(NEW.numOfAdults, 0) + COALESCE(NEW.numOfSeniors, 0);
END;
//

DELIMITER ;

SELECT b.barangayName, b.barangayDistance, COUNT(e.incidentNumber) AS emergencyCount
FROM Barangay b
LEFT JOIN Emergency e ON b.barangayID = e.barangayID
GROUP BY b.barangayID, b.barangayName, b.barangayDistance