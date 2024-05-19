package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 *By using this method, the data is printed as text files in a base directory of your choice.
 *Every kind of alert has a file to which all pertinent alert data is appended.
 *<p>
 *@author Yalcin Karakurum
 *@version 1.0.0
 * </p>
 **/

public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * This outputs the specified alert data to the relevant file for a particular patient.
     * <p>
     * Files are kept in the base directory and are titled according to the type of alert.
     * A prepared appendix containing alert data is attached to the file.
     * </p>
     *
     * @param patientId the ID of the patient
     * @param timestamp the time at which the alert was generated
     * @param label  the label/type of the alert
     * @param data   the associated data with the alert
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}