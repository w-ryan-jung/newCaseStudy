package matchit.base.server.stock;

import matchit.base.server.Config;
import matchit.base.server.database.BaseDataAccessTest;
import matchit.base.server.database.DataAccessException;
import matchit.base.server.location.LocationDataAccess;
import matchit.base.server.product.ProductDataAccess;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Najwa Mekdad, na6363me@student.lu.se
 */
public class StockDataAccessTest extends BaseDataAccessTest {

	private StockDataAccess stockDao = new StockDataAccess(Config.instance().getDatabaseDriver());
	private LocationDataAccess locationDao = new LocationDataAccess(Config.instance().getDatabaseDriver());
	private ProductDataAccess productDao = new ProductDataAccess(Config.instance().getDatabaseDriver());

    @Test
    public void getNoStock() {
        assertTrue(stockDao.getAllStock().isEmpty());
        assertTrue(stockDao.getUserStock(1).isEmpty());
        assertTrue(stockDao.getUserStock(2).isEmpty());
    }

    @Test(expected = DataAccessException.class)
    public void addToNoOne() {
    	locationDao.addLocation("location1");
    	productDao.addProduct("product1", 5000);
        stockDao.addStock(-10, "location1", "product1", 500);
    }

    @Test
    public void addToUser() {
    	
    	locationDao.addLocation("user1s data1");
    	productDao.addProduct("user1s data2", 5000);
        Stock data = stockDao.addStock(TEST.getId(), "user1s data1", "user1s data2", 500);
        assertEquals(TEST.getId(), data.getUserId());
        assertEquals("user1s data1", data.getLocationName());
        assertEquals("user1s data2", data.getProductName());
        assertEquals(500, data.getAmount());
    }

    @Test
    public void getAllDataFromDifferentUsers() {
    	locationDao.addLocation("location1");
    	productDao.addProduct("product1", 5000);
        stockDao.addStock(TEST.getId(), "location1", "product1", 50);
        assertEquals(1, stockDao.getAllStock().size());
        locationDao.addLocation("location2");
    	productDao.addProduct("product2", 5000);
        stockDao.addStock(ADMIN.getId(), "location2", "product2", 100);
        assertEquals(2, stockDao.getAllStock().size());
        assertEquals(2L, stockDao.getAllStock().stream().map(Stock::getUserId).distinct().count());
    }

    @Test
    public void getAllDataFromDefinedUser() {
        locationDao.addLocation("location1");
        productDao.addProduct("product1", 5000);
        stockDao.addStock(TEST.getId(), "location1", "product1", 50);
        locationDao.addLocation("location2");
        productDao.addProduct("product2", 5000);
        stockDao.addStock(ADMIN.getId(), "location2", "product2", 100);
        assertEquals(1, stockDao.getUserStock(1).size());
        assertEquals(1L, stockDao.getUserStock(1).stream().map(Stock::getUserId).distinct().count());
        assertEquals(1, stockDao.getUserStock(2).size());
        assertEquals(1L, stockDao.getUserStock(2).stream().map(Stock::getUserId).distinct().count());

    }




}
