package matchit.base.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helper to make working with JDBC's checked exception easier. A Mapper is defined for each type we want to extract
 * The function {@link Mapper#map(ResultSet)} is called once for each row in the result set.
 *
 * @param <T> Java type of mapped object.
 * @author Rasmus Ros, rasmus.ros@cs.lth.se
 */
public interface Mapper<T> {
    T map(ResultSet resultSet) throws SQLException;
}
