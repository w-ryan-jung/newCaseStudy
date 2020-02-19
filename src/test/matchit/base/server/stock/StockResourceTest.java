package matchit.base.server.stock;

import matchit.base.server.BaseResourceTest;
import matchit.base.server.Config;
import matchit.base.server.location.LocationDataAccess;
import matchit.base.server.product.ProductDataAccess;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

public class StockResourceTest extends BaseResourceTest {

    private static final GenericType<List<Stock>> STOCK_LIST = new GenericType<List<Stock>>() {
    };
    private LocationDataAccess locationDao = new LocationDataAccess(Config.instance().getDatabaseDriver());
    private final ProductDataAccess productDao = new ProductDataAccess(Config.instance().getDatabaseDriver());


    @Before
    public void loginTest() {
        login(ADMIN_CREDENTIALS);
        locationDao.addLocation(("location1"));
        locationDao.addLocation("product1");
        productDao.addProduct("product1", 5000);
        productDao.addProduct("product2", 7000);
        logout();
        login(TEST_CREDENTIALS);
    }

    @Test(expected = ForbiddenException.class)
    public void getStockAsNoOne() {
        logout();
        target("stock").request().get(STOCK_LIST);
    }

    @Test
    public void getNoStockData() {
        List<Stock> list = target("stock").request().get(STOCK_LIST);
        assertTrue(list.isEmpty());
    }

    @Test
    public void addStock() {
        Stock newStock= new Stock(1, 2, "location1", "product1", 50, System.currentTimeMillis());
        Stock stock = target("stock").request()
                .post(Entity.json(newStock), Stock.class);
        assertEquals(50, stock.getAmount());
        assertEquals("location1", stock.getLocationName());
        assertEquals("product1", stock.getProductName());
        assertNotEquals(0, stock.getStockId());
        assertNotEquals(0, stock.getDate());
    }


    @Test
    public void addManyStock() {
        for (int i = 0; i < 10; i++) {
            Stock newStock = new Stock( i + 1, 2, "location1", "product1", 50, System.currentTimeMillis());
            target("stock").request()
                    .post(Entity.json(newStock), Stock.class);
        }
        List<Stock> list = target("stock").request().get(STOCK_LIST);
        assertEquals(10, list.size());

    }


    @Test(expected = ForbiddenException.class)
    public void getAllDataAsUser() {
        target("stock").path("all").request().get(STOCK_LIST);
    }
    /**
    @Test
    public void getStocks() {
        Stock newStock= new Stock(1, 2, "location1", "product1", 50, System.currentTimeMillis());
        Stock stock = target("stock").request()
                .post(Entity.json(newStock), Stock.class);
        List<Stock> testsStocks = target("stock")
                .path("user")
                .path(Integer.toString(TEST.getId()))
                .request()
                .get(STOCK_LIST);
        assertEquals(50, testsStocks.get(0).getAmount());
    }
    */
    @Test
    public void getAllDataAsAdmin() {
               logout();
        // Post data as Admin user
        login(ADMIN_CREDENTIALS);
        Stock newStock= new Stock(1, 2, "location1", "product1", 50, System.currentTimeMillis());
        Stock stock = target("stock").request()
                .post(Entity.json(newStock), Stock.class);

        // There should be data from two users
        List<Stock> stocks = target("stock").path("all").request().get(STOCK_LIST);
        assertEquals(50, stocks.get(0).getAmount());
        assertEquals("location1", stocks.get(0).getLocationName());
        assertEquals("product1", stocks.get(0).getProductName());
        assertNotEquals(0, stocks.get(0).getStockId());
        assertNotEquals(0, stocks.get(0).getDate());
    }

    @Test
    public void getSomeonesDataAsAdmin() {
        // Post data as Test user
        // Post data as Test user
        Stock newStock= new Stock(1, 2, "location1", "product1", 50, System.currentTimeMillis());
        Stock stock = target("stock").request()
                .post(Entity.json(newStock), Stock.class);
        logout();

        // Get Test's data as Admin
        login(ADMIN_CREDENTIALS);
        List<Stock> stocks = target("stock")
                .path("user")
                .path(Integer.toString(TEST.getId()))
                .request()
                .get(STOCK_LIST);
        assertEquals(50, stocks.get(0).getAmount());
        assertEquals("location1", stocks.get(0).getLocationName());
        assertEquals("product1", stocks.get(0).getProductName());
        assertNotEquals(0, stocks.get(0).getStockId());
        assertNotEquals(0, stocks.get(0).getDate());

    }
}
