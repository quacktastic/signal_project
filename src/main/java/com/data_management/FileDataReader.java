package com.data_management;
import java.io.*;

import java.io.IOException;

public class FileDataReader implements DataReader {

    /**
     * This class Reads File data with the method of DataReader interface
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException
     */

    private String directoryPath;
    private DataParser dataParser;


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
            for(File file : directory.listFiles()) {
                if(file.isFile() && file.getName().endsWith(".txt")) {
                    parseFile(file, dataStorage);
                }
            }
        } else {
            System.out.println("Provided path is not a directory.");
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
            while ((line = reader.readLine()) != null) {
                dataParser.parse(line, dataStorage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
