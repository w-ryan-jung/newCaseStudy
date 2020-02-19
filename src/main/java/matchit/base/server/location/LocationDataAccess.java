package matchit.base.server.location;

import matchit.base.server.database.DataAccess;
import matchit.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LocationDataAccess extends DataAccess<Location> {
    private static final class LocationMapper implements Mapper<Location> {

        @Override
        public Location map(ResultSet resultSet) throws SQLException {
            return new Location(resultSet.getString("location_name"));
        }
    }

    public LocationDataAccess(String driverUrl){
        super(driverUrl, new LocationMapper());
    }

    public Location addLocation(String location){
        insert("INSERT INTO location (location_name) VALUES (?)",
                location);
        return new Location(location);
    }

    public List<Location> getLocations() {
        return query("SELECT * FROM location");
    }

    public boolean updateLocation(String location){
        return execute("UPDATE location SET location_name = ?",location) > 0;
    }

    public boolean deleteLocation(String location){
        return execute("DELETE FROM location WHERE location_name = ?",location) > 0;
    }
}
