package com.data_management;

public class DataParser {

    /**
     * Parses a line of text and stores the parsed data in DataStorage
     *
     * @param line        the line of the text to be parsed
     * @param dataStorage the DataStorage instace to store parsed data
     */

    public static void parse(String line, DataStorage dataStorage) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            try {
                int patientId = Integer.parseInt(parts[0]);
                double measurementValue = Double.parseDouble(parts[1]);
                String recordType = parts[2];
                long timestamp = Long.parseLong(parts[3]);
                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            } catch (NumberFormatException e) {
                System.out.println("Invalid data format: " + line);
            }
        } else {
            System.out.println("Invalid data format: " + line);
        }
    }
}

