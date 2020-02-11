package matchit.base.server.stock;

import matchit.base.server.database.DataAccess;
import matchit.base.server.database.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class StockDataAccess extends DataAccess<Stock> {

    private static final class StockMapper implements Mapper<Stock>{

        @Override
        public Stock map(ResultSet resultSet) throws SQLException {
            return new Stock(resultSet.getInt("stock_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("location_name"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("amount"),
                    resultSet.getObject("date", Date.class).getTime());
        }
    }

    public StockDataAccess(String driverUrl){
        super(driverUrl, new StockMapper());
    }

    public Stock addStock(int userId, String locationName, String productName, int amount){
        long date = System.currentTimeMillis();
        int stockId = insert("INSERT INTO stock (user_id, location_name, product_name, amount, date) VALUES (?,?,?,?,?)",
                userId, locationName, productName, amount, new Date(date));
        return new Stock(stockId, userId, locationName, productName, amount, date);
    }

    public List<Stock> getAllStock(){
        return query("SELECT * FROM stock");
    }

    public List<Stock> getUserStock(int userId){
        return query("SELECT * FROM stock WHERE user_id = ?",userId);
    }


}
