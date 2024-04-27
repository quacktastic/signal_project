package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * This method implements the {@link OutputStrategy} to output patient data over TCP
 * <p>
 *  This class sets up a TCP server on the specified port and waits for clients to connect.
 *  Once a client is connected, it sends the patient data over this connection.
 * </p>
 */

public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * This one creates a TCP server on the specified port to send out patient data
     * <p>
     *  The server is started in a new thread, allowing the application to continue running without blocking.
     *  It waits for a client to connect to send the data
     * </p>
     *
     * @param port the port number on which the server will listen for connections.
     */

    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs the patient data over the established TCP connection.
     * <p>
     * This method formats the data into a CSV style string and sends it to the connected client.
     * If no client is connected, the data is not sent.
     * </p>
     *
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data generation
     * @param label     the label describing the type of data
     * @param data      the actual data to be output
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
