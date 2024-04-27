package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * An interface for generating patient-related data
 * <p>
 *  This interface defines the contract for implementing data generation for a patient. Implementations of this
 *  interface should handle the specifics of data generation for various health metrics.
 * </p>
 */

public interface PatientDataGenerator {

    /**
     * This generates data for a single patient and outputs it using the provided output strategy.
     * <p>
     *  Implementations should use this method to produce data specific to the type of generator and output
     *  it through the given {@link OutputStrategy}.
     * </p>
     * @param patientId   the unique identifier for the patient
     * @param outputStrategy the strategy to output generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
