package com.data_management;
import java.io.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.SimpleFormatter;

public class FileDataReader implements DataReader {

    private static final Logger logger = Logger.getLogger(FileDataReader.class.getName());

    private String directoryPath;
    private DataParser dataParser;

    static {
        try  {
            // Connecting logging.properties
            LogManager.getLogManager().readConfiguration(FileDataReader.class.getResourceAsStream("/logging.properties"));
          } catch (IOException e) {
            logger.severe("Failed to load logging configuration: " + e.getMessage());
         }
      }



    /**
     *
     * @param directoryPath the path to the directory containing the data files
     * @param dataParser the DataParser instance to parse the file content
     */
    public FileDataReader(String directoryPath, DataParser dataParser) {
        this.directoryPath = directoryPath;
        this.dataParser = dataParser;
    }


   @Override
   public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(directoryPath);
        if(directory.isDirectory()) {
            logger.info("Reading directory: " + directoryPath);
            for(File file : directory.listFiles()) {
                if(file.isFile() && file.getName().endsWith(".txt")) {
                    logger.info("Reading file: " + file.getName());
                    parseFile(file, dataStorage);
                }
            }
        } else {
            logger.severe("Provided path is not a directory. " + directoryPath);
        }
    }


    /**
     * Parses the content of the specified file and stores the parsed data in DataStorage
     * @param file the file to be parsed
     * @param dataStorage the dataStorage instance to store parsed data
     * @throws IOException if an I/O error occurs
     */
    private void parseFile(File file ,DataStorage dataStorage) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                logger.info("Parsing line: " + lineNumber + line);
                dataParser.parse(line, dataStorage);
            }
        } catch (IOException e) {
            logger.severe("Failed to read file: " + file.getName() + e.getMessage());
            throw e;
        }

      }

    }


