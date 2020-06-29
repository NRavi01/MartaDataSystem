import com.martasim.datamgmt.Database;
import com.martasim.datamgmt.DatabaseFactory;
import com.martasim.models.Bus;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        if (args.length != 1) {
            System.out.println("Usage: java Main [GTFS formatted file name]");
        }
        Database db = DatabaseFactory.createDatabaseFromGtfs(new File(args[0]));

        Collection<Bus> buses = db.getAllBuses();
        System.out.printf("There are %d buses\n", buses.size());
        for (Bus bus : buses) {
            System.out.println(bus);
        }
    }
}
