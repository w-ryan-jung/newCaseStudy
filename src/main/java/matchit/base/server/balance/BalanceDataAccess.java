package matchit.base.server.balance;

import matchit.base.server.database.DataAccess;
import matchit.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BalanceDataAccess extends DataAccess<Balance> {

    private static final class BalanceMapper implements Mapper<Balance> {

        @Override
        public Balance map(ResultSet resultSet) throws SQLException {
            return new Balance(resultSet.getString("location_name"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("total"));
        }
    }

    public BalanceDataAccess(String driverUrl){
        super(driverUrl, new BalanceMapper());
    }

    public List<Balance> getTotalByLocation(String location){
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having location_name = ?",location);
    }

    public List<Balance> getTotalByProduct(String product){
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having product_name = ?",product);
    }

    public List<Balance> getTotal(){
        return query("SELECT location_name, product_name, SUM(amount) AS total " +
                "FROM stock " +
                "GROUP BY location_name, product_name " +
                "HAVING total > 0");
    }

}
