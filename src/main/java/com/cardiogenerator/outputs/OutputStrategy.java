package com.cardiogenerator.outputs;

/**
 * A strategy interface for outputting patient data
 * <p>
 *  It defines the method that must be implemented to output data for a patient
 *  Implementing classes can output the data to various destinations such as console, file, network socket, etc.
 * </p>
 */
public interface OutputStrategy {

    /**
     * gives the patient's specified data as an output.
     * <p>
     *  This method's actual data output is supposed to be handled by implementations.
     * </p>
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data generation
     * @param label     the label describing the type of data
     * @param data      the actual data to be output
     */
    void output(int patientId, long timestamp, String label, String data);
}
