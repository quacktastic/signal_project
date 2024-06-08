# Cardio Data Simulator

The Cardio Data Simulator is a Java-based application designed to simulate real-time cardiovascular data for multiple patients. This tool is particularly useful for educational purposes, enabling students to interact with real-time data streams of ECG, blood pressure, blood saturation, and other cardiovascular signals.

## Features

- Simulate real-time ECG, blood pressure, blood saturation, and blood levels data.
- Supports multiple output strategies:
  - Console output for direct observation.
  - File output for data persistence.
  - WebSocket and TCP output for networked data streaming.
- Configurable patient count and data generation rate.
- Randomized patient ID assignment for simulated data diversity.

## Getting Started

### Prerequisites

- Java JDK 11 or newer.
- Maven for managing dependencies and compiling the application.

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/tpepels/signal_project.git
   ```

2. Navigate to the project directory:

   ```sh
   cd signal_project
   ```

3. Compile and package the application using Maven:
   ```sh
   mvn clean package
   ```
   This step compiles the source code and packages the application into an executable JAR file located in the `target/` directory.

### Running the Simulator

After packaging, you can run the simulator directly from the executable JAR:

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar
```

To run with specific options (e.g., to set the patient count and choose an output strategy):

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar --patient-count 100 --output file:./output
```

### Supported Output Options

- `console`: Directly prints the simulated data to the console.
- `file:<directory>`: Saves the simulated data to files within the specified directory.
- `websocket:<port>`: Streams the simulated data to WebSocket clients connected to the specified port.
- `tcp:<port>`: Streams the simulated data to TCP clients connected to the specified port.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.





## STUDENT ID

 Student ID: I6362856



## UML Models

The UML diagrams and their descriptions can be found in the [uml_models](uml_models) directory.

- [Alert Generation System Class Diagram](uml_models/Alert%20Generation%20System/UML%20Class%20Diagram/AlertGeneratorClassUpdated.pdf)
- [Alert Generation System Sequence Diagram](uml_models/Alert%20Generation%20System/UML%20Sequence%20Diagram/AlertGeneratorClassUpdated.pdf)
- [Alert Generation System State Diagram](uml_models/Alert%20Generation%20System/UML%20State%20Diagram/AlertGeneratorClassUpdated.pdf)
- [Data Access Layer System Diagram](uml_models/Data%20Access%20Layer%20System/)
- [Data Storage System Diagram](uml_models/Data%20Storage%20System/)
- [Patient Identification System Diagram](uml_models/Patient%20Identification%20System/)


