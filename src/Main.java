import com.martasim.datamgmt.Database;
import com.martasim.datamgmt.DatabaseFactory;
import com.martasim.models.Bus;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        if (args.length != 1) {
            System.out.println("Usage: java Main [GTFS formatted file name]");
        }
        Database db = DatabaseFactory.createDatabaseFromGtfs(new File(args[0]));

        List<Bus> busses = db.getAllBusses();
        System.out.printf("There are %d busses\n", busses.size());
        for (Bus bus : busses) {
            System.out.println(bus);
        }
    }
}
