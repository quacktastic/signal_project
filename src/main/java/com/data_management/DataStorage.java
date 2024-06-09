package com.data_management;

import com.alerts.AlertGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    public DataStorage() {
        this.patientMap = new HashMap<>();
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to
     * the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // DataReader is not defined in this scope, should be initialized appropriately.
        // DataReader reader = new SomeDataReaderImplementation("path/to/data");
        if(args.length < 2 || !args[0].equals("--output")) {
            System.out.println("Invalid arguments. Usage: DataStorage --output <outputFolder> ");
            return;
        }


        DataStorage storage = new DataStorage();

        ;

            String outputFolder = args[1];
            System.out.println("Patient data directory " + outputFolder);


            File folder = new File(outputFolder);
            File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

            if(listOfFiles == null) {
                System.out.println("No files found in the specified directory.");
                return;
            }

            for (File file : listOfFiles) {
                if (file.isFile()) {

                    System.out.println("Patient data file " + file.getName());

                    List<PatientRecord> patientRecordList = storage.deserialize(file.getPath());

                    for (PatientRecord patientRecord : patientRecordList) {

                        storage.addPatientData(patientRecord.getPatientId(),
                            patientRecord.getMeasurementValue(),
                            patientRecord.getRecordType(),
                            patientRecord.getTimestamp());

                        System.out.println("Patient data added");
                    }
                }
            }


        //FileDataReader fileDataReader = new FileDataReader(".txt", );
        // Assuming the reader has been properly initialized and can read data into the
        // storage
        // reader.readData(storage);

        List<Patient> allPatients = storage.getAllPatients();
        for (Patient patient : allPatients) {

            // Example of using DataStorage to retrieve and print records for a patient
            List<PatientRecord> records = storage.getRecords(patient.getPatientId(), 1700000000000L, 1800000000000L);
            for (PatientRecord record : records) {
                System.out.println("Record for Patient ID: " + record.getPatientId() +
                        ", Type: " + record.getRecordType() +
                        ", Data: " + record.getMeasurementValue() +
                        ", Timestamp: " + record.getTimestamp());
            }
        }

        // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Evaluate all patients' data to check for conditions that may trigger alerts
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }

    private void serialize(List<PatientRecord> value, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(value, writer);
        writer.flush();
        writer.close();
    }

    private List<PatientRecord> deserialize(String filePath) throws IOException {
        Type listType = new TypeToken<List<PatientRecord>>(){}.getType();
        //Type listType = TypeToken.getParameterized(List.class, PatientRecord).getType();

        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //return gson.fromJson(new FileReader(filePath), listType);
        return new Gson().fromJson(new FileReader(filePath), listType);
    }
}
