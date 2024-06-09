import com.data_management.DataStorage;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(args);
        } else {
           // HealthDataSimulator.main(new String[]{});
        }
    }
}
