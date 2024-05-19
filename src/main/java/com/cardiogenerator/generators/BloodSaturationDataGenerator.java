package com.cardiogenerator.generators;

import java.util.Random;

import com.cardiogenerator.outputs.OutputStrategy;

/**
 * This class generates simulated blood saturation data for patients.
 * <p>
 *  This class simulates blood oxygen saturation levels for a specified number of patients
 *  It is designed to generate realistic saturation fluctuations within a healthy range.
 * </p>
 */

public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;


    /**
     * This constructor constructs a BloodSaturationDataGenerator for the specified number of patients.
     * <p>
     *  Initializes baseline saturation values for each patient between 95 and 100, which represents
     *  typical healthy blood oxygen levels.
     * </p>
     *
     * @param patientCount the number of patients to generate data for
     */

    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates and outputs new blood saturation data for a specified patient.
     * <p>
     *  This method simulates small fluctuations in blood saturation and ensures that the values
     *  remain within a realistic and healthy range (90 to %100)
     *  The generated data is output using the provided {@link OutputStrategy}.
     * </p>
     *
     * @param patientId   the unique identifier for the patient
     * @param outputStrategy the strategy to output generated data
     */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
