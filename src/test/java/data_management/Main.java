package data_management;

import com.data_management.DataStorage;

public class Main {

    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(new String[]{});
        }
    }
}
