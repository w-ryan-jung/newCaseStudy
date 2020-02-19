package matchit.base.server.total;

import matchit.base.server.Config;
import matchit.base.server.database.BaseDataAccessTest;
import matchit.base.server.location.LocationDataAccess;
import matchit.base.server.product.ProductDataAccess;
import matchit.base.server.stock.StockDataAccess;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Najwa Mekdad, na6363me@student.lu.se
 */
public class TotalDataAccessTest extends BaseDataAccessTest {

    private TotalDataAccess totalDao = new TotalDataAccess(Config.instance().getDatabaseDriver());
    private StockDataAccess stockDao = new StockDataAccess(Config.instance().getDatabaseDriver());
    private LocationDataAccess locationDao = new LocationDataAccess(Config.instance().getDatabaseDriver());
    private ProductDataAccess productDao = new ProductDataAccess(Config.instance().getDatabaseDriver());
    
    
    @Test
    public void getNoTotal() {
        assertTrue(totalDao.getTotal().isEmpty());
    
    }

    @Test
    public void getAllTotal() {
    	locationDao.addLocation("location1");
    	locationDao.addLocation("location2");
    	productDao.addProduct("product1", 5000);
    	productDao.addProduct("product2", 7000);
        stockDao.addStock(TEST.getId(), "location1", "product1",500);
        stockDao.addStock(TEST.getId(), "location2", "product2",1000);
        stockDao.addStock(TEST.getId(), "location1","product1", 500);
        stockDao.addStock(TEST.getId(), "location2", "product2", 1000);
        assertEquals(1000,totalDao.getTotal().get(0).getTotal());
        assertEquals(2000,totalDao.getTotal().get(1).getTotal());
   }
    
    @Test
    public void getAllTotalByProduct() {
    	locationDao.addLocation("location1");
    	locationDao.addLocation("location2");
    	productDao.addProduct("product1", 5000);
    	productDao.addProduct("product2", 7000);
    	stockDao.addStock(TEST.getId(), "location1", "product1", 500);
    	stockDao.addStock(TEST.getId(), "location1", "product1", 300);
        stockDao.addStock(TEST.getId(), "location2", "product2", 500);
        stockDao.addStock(TEST.getId(), "location2", "product2", 1000);
        assertEquals(800,totalDao.getTotalByProduct("product1").get(0).getTotal());
        assertEquals(1500,totalDao.getTotalByProduct("product2").get(0).getTotal());
        
   } 
    
    @Test
    public void getAllTotalByLocation() {
    	locationDao.addLocation("location1");
    	locationDao.addLocation("location2");
    	productDao.addProduct("product1", 5000);
    	productDao.addProduct("product2", 7000);
    	stockDao.addStock(TEST.getId(), "location1", "product1", 500);
        stockDao.addStock(TEST.getId(), "location2", "product2",1000);
        stockDao.addStock(TEST.getId(), "location1","product1", 1000);
        stockDao.addStock(TEST.getId(), "location2", "product2",1000);
        
        List<Total> totals = totalDao.getTotalByLocation("location1");
        assertEquals(1500,totalDao.getTotalByLocation("location1").get(0).getTotal());
        assertEquals(2000,totalDao.getTotalByLocation("location2").get(0).getTotal());
   } 
    
    @Test
    public void getAllTotalByProductAndLocation() {
    	locationDao.addLocation("location1");
    	locationDao.addLocation("location2");
    	productDao.addProduct("product1", 5000);
    	productDao.addProduct("product2", 7000);
    	stockDao.addStock(TEST.getId(), "location1","product1", 1000);
        stockDao.addStock(TEST.getId(), "location2","product2", 500);
        stockDao.addStock(TEST.getId(), "location1", "product1", 1000);
        stockDao.addStock(TEST.getId(), "location2","product2",1000);
        assertEquals(2000,totalDao.getTotalByLocationProduct( "location1", "product1").get(0).getTotal());
        assertEquals(1500,totalDao.getTotalByLocationProduct("location2", "product2").get(0).getTotal());
   } 
    
    
 
}
