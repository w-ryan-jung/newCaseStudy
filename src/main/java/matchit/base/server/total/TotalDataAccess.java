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

    public List<Total> getStocksBy(Total item){

        if(item.getLocationName().equals("All Location") && item.getProductName().equals("All Product")){
            return getTotal();
        }else if(!item.getLocationName().equals("All Location") && !item.getProductName().equals("All Product")){
            return getTotalByLocationProduct(item.getLocationName(),item.getProductName());
        }else{
            if(!item.getLocationName().equals("All Location")){
                return getTotalByLocation(item.getLocationName());
            }else {
                return getTotalByProduct(item.getProductName());
            }
        }
    }

    public List<Total> getTotalByLocation(String location){
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having location_name = ?",location);
    }

    public List<Total> getTotalByProduct(String product){
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having product_name = ?",product);
    }

    public List<Total> getTotalByLocationProduct(String location, String product){
        return query("select location_name, product_name, sum(amount) as total from stock group by location_name, product_name having location_name = ? and product_name = ?",location,product);
    }

    public List<Total> getTotal(){
        return query("SELECT location_name, product_name, SUM(amount) AS total " +
                "FROM stock " +
                "GROUP BY location_name, product_name");
    }

}
