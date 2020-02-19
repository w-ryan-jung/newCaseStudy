package matchit.base.server.product;

import matchit.base.server.Config;
import matchit.base.server.database.BaseDataAccessTest;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Najwa Mekdad, na6363me@student.lu.se
 */
public class ProductDataAccessTest extends BaseDataAccessTest {

    private ProductDataAccess productDao = new ProductDataAccess(Config.instance().getDatabaseDriver());

    @Test
    public void addProduct() {
    	 Product product = productDao.addProduct("data1", 1000);
         assertTrue(productDao.deleteProduct("data1"));
    }
    
    @Test
    public void deleteProduct() {
    	 Product product = productDao.addProduct("data1", 1000);
         assertTrue(productDao.deleteProduct("data1"));
    }

    
    @Test
    public void deleteMissingProduct() {
        // No product has been added yet, so the product with name = "data1" should not exist
        String product = "data1";
        assertFalse(productDao.deleteProduct(product));
    }

    @Test
    public void getProducts() {
        productDao.addProduct("product1", 5000);
        assertEquals(1,  productDao.getProducts().size());
        productDao.addProduct("product2", 7000);
        assertEquals(2, productDao.getProducts().size());
        assertEquals(2L, productDao.getProducts().stream().map(Product::getProductName).distinct().count());
    }

    @Test
    public void updateProduct() {
        Product product= productDao.addProduct("product1", 5000);
        productDao.updateProduct("product2");
        assertEquals("product2", productDao.getProducts().get(0).getProductName());
    }
}
