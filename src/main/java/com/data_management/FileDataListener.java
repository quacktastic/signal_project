package com.data_management;

import java.io.*;

public class FileDataListener extends DataListener {

    private String filepath;
    private DataParser dataParser;
    private DataStorage dataStorage;


    /**
     *
     * Constructs a new FileDataListener with the specified file path, data parser, and data storage
     *
     * @param filepath
     * @param dataParser
     * @param dataStorage
     **/
     public FileDataListener(String filepath, DataParser dataParser, DataStorage dataStorage) {

         this.filepath = filepath;
         this.dataParser = dataParser;
         this.dataStorage = dataStorage;
     }


    /**
     * Listens for data from the specified file and parses it.
     **/
    @Override
    public void listen() {
         File file = new File(filepath);
         if(file.isFile() && file.getName().endsWith(".txt")) {
             parseFile(file);
         } else {
             System.out.println("Provided path is not a valid file.");
         }
     }

    /**
     * Parses the content of the specified file and stores the parsed data in DataStorage.
     *
     * @param file the file to be parsed
     */

    private void parseFile(File file) {
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
