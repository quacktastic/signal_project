package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;

    private static final Logger logger = Logger.getLogger(AlertGenerator.class.getName());

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 1700000000000L, 1800000000000L);

        PatientRecord prevPrevRecord = null;
        PatientRecord prevRecord = null;
        for (PatientRecord record : records) {
            if(record == null) {
                logger.warning("Encountered a null record for patient ID: " + patient.getPatientId());
                continue;
            }
            logger.info("Processing record: " + record);

            String[] conditions = new String[]
                    {
                            bloodPressureCriticalThresholds(record),
                            bloodPressureIncreasingTrend(prevPrevRecord, prevRecord, record),
                            bloodPressureDecreasingTrend(prevPrevRecord, prevRecord, record),
                            lowSaturationAlert(record),
                            bloodSaturationRapidDrop(prevPrevRecord, prevRecord, record)
                    };

            for (String condition : conditions) {
                if (condition != null) {
                    triggerAlert(new Alert(patient.getPatientId(), condition, record.getTimestamp()));
                }
            }

            prevPrevRecord = prevRecord;
            prevRecord = record;

        }


    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        alerts.add(alert);
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    private String bloodPressureCriticalThresholds(PatientRecord record) {
        if (record.getRecordType().equals("blood pressure diastolic")) {
            if (record.getMeasurementValue() < 60) {
                return "diastolic blood pressure is too low";
            } else if (record.getMeasurementValue() > 120) {
                return "diastolic blood pressure is too high";
            }
        } else if (record.getRecordType().equals("blood pressure systolic")) {
            if (record.getMeasurementValue() < 90) {
                return "systolic blood pressure is too low";
            } else if (record.getMeasurementValue() > 180) {
                return "systolic blood pressure is too high";
            }
        }
        return null;
    }

    private String bloodPressureIncreasingTrend(PatientRecord record1, PatientRecord record2, PatientRecord record3) {
        if (record1 != null && record1.getRecordType().startsWith("blood pressure")) {
            if (record2 != null && record2.getRecordType().startsWith("blood pressure")) {
                if (record1.getMeasurementValue() < record2.getMeasurementValue()) {
                    if ((record2.getMeasurementValue() - record1.getMeasurementValue()) >= 10) {
                        return "blood pressure is in increasing trend";
                    }
                }
            }

            if (record3 != null && record3.getRecordType().startsWith("blood pressure")) {
                if (record1.getMeasurementValue() < record3.getMeasurementValue()) {
                    if ((record3.getMeasurementValue() - record1.getMeasurementValue()) >= 10) {
                        return "blood pressure is in increasing trend";
                    }
                }
            }
        }

        if (record2 != null && record2.getRecordType().startsWith("blood pressure")) {
            if (record3 != null && record3.getRecordType().startsWith("blood pressure")) {
                if (record2.getMeasurementValue() < record3.getMeasurementValue()) {
                    if ((record3.getMeasurementValue() - record2.getMeasurementValue()) >= 10) {
                        return "blood pressure is in increasing trend";
                    }
                }
            }
        }

        return null;
    }

    private String bloodPressureDecreasingTrend(PatientRecord record1, PatientRecord record2, PatientRecord record3) {
        if (record1 != null && record1.getRecordType().startsWith("blood pressure")) {
            if (record2 != null && record2.getRecordType().startsWith("blood pressure")) {
                if (record2.getMeasurementValue() < record1.getMeasurementValue()) {
                    if ((record1.getMeasurementValue() - record2.getMeasurementValue()) >= 10) {
                        return "blood pressure is in decreasing trend";
                    }
                }
            }

            if (record3 != null && record3.getRecordType().startsWith("blood pressure")) {
                if (record3.getMeasurementValue() < record1.getMeasurementValue()) {
                    if ((record1.getMeasurementValue() - record3.getMeasurementValue()) >= 10) {
                        return "blood pressure is in decreasing trend";
                    }
                }
            }
        }

        if (record2 != null && record2.getRecordType().startsWith("blood pressure")) {
            if (record3 != null && record3.getRecordType().startsWith("blood pressure")) {
                if (record3.getMeasurementValue() < record2.getMeasurementValue()) {
                    if ((record2.getMeasurementValue() - record3.getMeasurementValue()) >= 10) {
                        return "blood pressure is in decreasing trend";
                    }
                }
            }
        }

        return null;
    }

    private String lowSaturationAlert(PatientRecord record) {
        if (record.getRecordType().equals("blood saturation") && record.getMeasurementValue() < 92) {
            return "blood saturation level is too low";
        }
        return null;
    }

    private String bloodSaturationRapidDrop(PatientRecord record1, PatientRecord record2, PatientRecord record3) {

        if (record1 == null || record2 == null || record3 == null) {
            logger.warning("One of the records is null: record1 " + record1 + " record2 " + record2 + " record3 " + record3);
         return null;
        }

        if(record1.getRecordType().equals("blood saturation") &&
           record2.getRecordType().equals("blood saturation") &&
           record3.getRecordType().equals("blood saturation")) {
            long timeDifference1 = Math.abs(record1.getTimestamp() - record2.getTimestamp());
            long timeDifference2 = Math.abs(record2.getTimestamp() - record3.getTimestamp());
            long timeDifference3 = Math.abs(record1.getTimestamp() - record3.getTimestamp());


            boolean rapidDropBetween1and2 = timeDifference1 <= 600000 && (record1.getMeasurementValue() - record2.getMeasurementValue()) >= 5;
            boolean rapidDropBetween2and3 = timeDifference2 <= 600000 && (record2.getMeasurementValue() - record3.getMeasurementValue()) >= 5;
            boolean rapidDropBetween1and3 = timeDifference3 <= 600000 && (record1.getMeasurementValue() - record3.getMeasurementValue()) >= 5;

            if (rapidDropBetween1and2 || rapidDropBetween2and3 || rapidDropBetween1and3) {
                logger.info("Rapid drop alert: " + record1.getMeasurementValue() + " to " + record3.getMeasurementValue() + " within " + timeDifference3 / 60000 + " minutes ");
                return "blood oxygen saturation level dropped by 5% or more within 10 minutes";
            }
        }





        //if (rapidDropBetween1and2 && !rapidDropBetween1and3 && !rapidDropBetween2and3) {
          //  logger.info("Rapid drop alert: " + record1.getMeasurementValue() + " to " + record2.getMeasurementValue() + " within " + timeDifference1 / 60000 + " minutes ");
          //  return "blood oxygen saturation level dropped by 5% or more within 10 minutes";
      //  }
        //if (rapidDropBetween2and3 && !rapidDropBetween1and3 && !rapidDropBetween1and2) {
        //    logger.info("Rapid drop alert: " + record2.getMeasurementValue() + " to " + record3.getMeasurementValue() + " within " + timeDifference2 / 60000 + " minutes ");
        //    return "blood oxygen saturation level dropped by 5% or more within 10 minutes";
       // }
       // if (rapidDropBetween1and3 && !rapidDropBetween1and2 && !rapidDropBetween2and3) {
        //    logger.info("Rapid drop alert: " + record1.getMeasurementValue() + " to " + record3.getMeasurementValue() + " within " + timeDifference3 / 60000 + " minutes ");
        //    return "blood oxygen saturation level dropped by 5% or more within 10 minutes";

        //}
        return null;
    }
}


