package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


// NOTE (IMPORTANT!!!) : I had an error while doing the Blood Saturation Alert, the method I implemented failed 1 out of the 4 tests. I tried to fix it for at least 6 hours or something but
// I couldn't help it either. So I rather decided to try to continue rather than skipping the other steps.


/**
 * Test class for {@link DataStorage}.
 */
class DataStorageTest {
    private static final Logger logger = Logger.getLogger(DataStorageTest.class.getName());
    private DataStorage storage;
    private DataReader reader;
    private AlertGenerator alertGenerator;


    /**
     * Sets up the test environment before each test
     *
     * @throws IOException if an I/O error occurs
     */
    @BeforeEach
    void setUp() throws IOException {

        storage = new DataStorage();
        DataParser dataParser = new DataParser();
        reader = new FileDataReader("src/test/resources/test_data_directory", dataParser);

        Path testDir = Paths.get("src/test/resources/test_data_directory");
        Files.createDirectories(testDir);

        // Implemented test folders here
        Files.write(testDir.resolve("record1.txt"), List.of(
                "1,100.0,WhiteBloodCells,1714376789050",
                "1,200.0,WhiteBloodCells,1714376789051"
        ));

        Files.write(testDir.resolve("record2.txt"), List.of(
                "1,150.0,WhiteBloodCells,1714376789052",
                "1,250.0,WhiteBloodCells,1714376789053",
                "1,85.0,blood saturation,1714376789054",
                "1,90.0,blood saturation,1714376789055",
                "1,80.0,blood saturation,1714376789056"
        ));



        try {
            reader.readData(storage);
        } catch (IOException e) {
            logger.severe("Failed to read data during setup: " + e.getMessage());
            e.printStackTrace();
        }
       alertGenerator = new AlertGenerator(storage);
    }


    /**
     * Tests the addition and retrieval of records
     */
    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        logger.info("Running testAddAndGetRecords");

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789053L);
        assertEquals(4,records.size());

        // First record test
        assertEquals(150.0, records.get(0).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(0).getRecordType());
        assertEquals(1714376789050L, records.get(0).getTimestamp());

        // Second record test
        assertEquals(150.0,records.get(1).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(1).getRecordType());
        assertEquals(1714376789051L, records.get(1).getTimestamp());

        // Third record test
        assertEquals(200.0, records.get(2).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(2).getRecordType());
        assertEquals(1714376789052L, records.get(2).getTimestamp());

        // Fourth record test
        assertEquals(250.0, records.get(3).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(3).getRecordType());
        assertEquals(1714376789053L, records.get(3).getTimestamp());


        // Retrieve the record within the specified time stage
        records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        logger.info("Numbers of records retrieved: " + records.size());

        // Validation of the numbers of record retrieved
        assertEquals(2, records.size()); // Check if two records are retrieved

        // Validate the data in the first record
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(0).getRecordType());
        assertEquals(1714376789050L, records.get(0).getTimestamp());

        // Validate the data in the second record
        assertEquals(200.0, records.get(1).getMeasurementValue());
        assertEquals("WhiteBloodCells", records.get(1).getRecordType());
        assertEquals(1714376789051L, records.get(1).getTimestamp());
    }


    /**
     * Tests the generation of blood saturation alerts
     */
    @Test
    void bloodSaturationAlerts() {
        logger.info("Running testBloodSaturationAlerts");

        List<PatientRecord> records = storage.getRecords(1, 1700000003004L, 1700000004204L);
        assertEquals(3, records.size());

        // First record test for blood saturation
        assertEquals(85.0, records.get(0).getMeasurementValue());
        assertEquals("blood saturation", records.get(0).getRecordType());
        assertEquals(1700000003004L, records.get(0).getTimestamp());

        // Second record test for blood saturation
        assertEquals(80.0, records.get(1).getMeasurementValue());
        assertEquals("blood saturation", records.get(1).getRecordType());
        assertEquals(1700000003604L, records.get(1).getTimestamp());

        // Third record test for blood saturation
        assertEquals(75.0, records.get(2).getMeasurementValue());
        assertEquals("blood saturation", records.get(2).getRecordType());
        assertEquals(1700000004204L, records.get(2).getTimestamp());

        // Check for low saturation alert
        alertGenerator.evaluateData(storage.getAllPatients().get(0));

        List<Alert> alerts = alertGenerator.getAlerts();
        logger.info("Number of alerts generated: " + alerts.size());
        alerts.forEach(alert -> logger.info("Alert " + alert.getCondition() + " at " + alert.getTimestamp()));


        assertEquals(6, alerts.size());

        // Check for low saturation alert
        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood saturation level is too low") && alert.getTimestamp() == 1700000003004L),
                "Expected alert 'blood saturation level is too low' at timestamp 1700000003004L not found.");

        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood saturation level is too low") && alert.getTimestamp() == 1700000003604L),
                "Expected alert 'blood saturation level is too low' at timestamp 1700000003604L not found.");

        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood saturation level is too low") && alert.getTimestamp() == 1700000004204L),
                "Expected alert 'blood saturation level is too low' at timestamp 1700000004204L not found.");

        // Check for rapid drop alert
        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood oxygen level is dropped by 5% or more within 10 minutes") && alert.getTimestamp() == 1700000003004L),
                "Expected alert 'blood oxygen level is dropped 5% or more within 10 minutes' at timestamp 1714376789054L not found. " );
        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood oxygen level is dropped by 5% or more within 10 minutes") && alert.getTimestamp() == 1700000003604L),
                "Expected alert 'blood oxygen level is dropped 5% or more within 10 minutes' at timestamp 1714376789055L not found. " );
        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("blood oxygen level is dropped by 5% or more within 10 minutes") && alert.getTimestamp() == 1700000004204L),
                "Expected alert 'blood oxygen level is dropped 5% or more within 10 minutes' at timestamp 1714376789056L not found. " );
    }


    /**
     * Tests the retrieval of records for a non-existent patient
     */
    @Test
    void testGetRecordsForNonExistentPatient() {
        logger.info("Running testGetRecordsForNonExistentPatient");

        List<PatientRecord> records = storage.getRecords(999, 1714376789050L, 1714376789053L);
        logger.info("Number of records retrieved for non-existent patient: " + records.size());

        assertEquals(0, records.size());
    }

    /**
     * Tests the retrieval of all patients
     */

    @Test
    void testGetAllPatients() {
        logger.info("Running testGetAllPatients");

        List<Patient> allPatients = storage.getAllPatients();
        logger.info("Number of patients retrieved: " + allPatients.size());

        assertEquals(1, allPatients.size());
        assertEquals(1, allPatients.get(0).getPatientId());

    }

    /**
     * Tests the generation of hypotensive hypoxemia alerts.
     */
    @Test
    void hypotensiveHypoxemiaAlertTest() {
        logger.info("Running hypotensiveHypoxemiaAlertTest");

        storage.addPatientData(1, 85.0, "blood pressure systolic", 1714376789050L);
        storage.addPatientData(1, 80.0, "blood saturation", 1714376789051L);

        alertGenerator.evaluateData(storage.getAllPatients().get(0));

        List<Alert> alerts = alertGenerator.getAlerts();
        logger.info("Number of alerts generated: " + alerts.size());
        alerts.forEach(alert -> logger.info("Alert " + alert.getCondition() + " at " + alert.getTimestamp()));


        assertTrue(alerts.stream().anyMatch(alert -> alert.getCondition().equals("Hypotensive Hypoxemia Alert: systolic blood pressure is below 90mmHg and blood oxygen saturation falls below 92%")
        && alert.getTimestamp()== 1714376789051L),
                "Expected alert 'Hypotensive Hypoxemia Alert: systolic blood pressure is below 90mmHg and blood oxygen saturation falls below 92%' not found.");
    }
}
