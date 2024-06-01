package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataParser;
import com.data_management.DataReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {




    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
         DataReader reader = new DataReader() {
             @Override
             public void readData(DataStorage dataStorage) throws IOException {
                 dataStorage.addPatientData(1,100.0, "WhiteBloodCells", 1714376789050L);
                 dataStorage.addPatientData(1,200.0,"WhiteBloodCells", 1714376789051L);
             }
         };



         // Created DataStorage with the mock DataReader
        DataStorage storage = new DataStorage();
        try {
            reader.readData(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Retrieve the record within the specified time stage
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

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
}
