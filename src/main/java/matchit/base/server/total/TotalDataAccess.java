package matchit.base.server.total;

import matchit.base.server.database.DataAccess;
import matchit.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TotalDataAccess extends DataAccess<Total> {

    private static final class BalanceMapper implements Mapper<Total> {

        @Override
        public Total map(ResultSet resultSet) throws SQLException {
            return new Total(resultSet.getString("location_name"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("total"));
        }
    }

    public TotalDataAccess(String driverUrl){
        super(driverUrl, new BalanceMapper());
    }

    public List<Total> getTotalByLocation(String location){
        System.out.println(location);
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having location_name = ?",location);
    }

    public List<Total> getTotalByProduct(String product){
        System.out.println(product);
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having product_name = ?",product);
    }

    public List<Total> getTotal(){
        return query("SELECT location_name, product_name, SUM(amount) AS total " +
                "FROM stock " +
                "GROUP BY location_name, product_name " +
                "HAVING total > 0");
    }

}
