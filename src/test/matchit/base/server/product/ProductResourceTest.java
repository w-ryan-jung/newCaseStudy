package matchit.base.server.product;

import matchit.base.server.BaseResourceTest;
import matchit.base.server.Config;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ProductResourceTest extends BaseResourceTest {
	private static final GenericType<List<Product>> PRODUCT_LIST = new GenericType<List<Product>>() {
    };
	private final ProductDataAccess productDao = new ProductDataAccess(Config.instance().getDatabaseDriver());
    
    @Before
    public void loginAdmin() {
        login(ADMIN_CREDENTIALS);
    }

    @Test
    public void addProduct() {
    	Product product = target("product").request()
                .post(Entity.json(Collections.singletonMap("productName","product1")), Product.class);
        assertEquals("product1", product.getProductName());
    }
    

    @Test //(expected = ForbiddenException.class)
    public void addProductAsUser() {
    	logout();
    	login(TEST_CREDENTIALS);
        Product product = target("product").request()
                .post(Entity.json(Collections.singletonMap("productName","product1")), Product.class);
        assertEquals("product1", product.getProductName());
    }



    @Test
    public void addManyProduct() {
    	for (int i = 0; i < 10; i++) {
        	Product product = target("product").request()
                    .post(Entity.json(Collections.singletonMap("productName", "product1" + i)), Product.class);
           assertEquals("product1" + i, product.getProductName());
        }
        List<Product> list = target("product").request().get(PRODUCT_LIST);
        assertEquals(10, list.size());
    }

    @Test
    public void getNoProductData() {
        List<Product> list = target("product").request().get(PRODUCT_LIST);
        assertTrue(list.isEmpty());
    }

    @Test
    public void updateProduct() {
        Product product = target("product")
                .request()
                .post(Entity.json(Collections.singletonMap("productName", "product1")), Product.class);
        target("product")
                .path("productName")
                .path("product1")
                .request()
                .post(Entity.json(null));
        List<Product> products = target("product").request().get(new GenericType<List<Product>>(){});
        assertEquals("product1", products.get(0).getProductName());
    }

    @Test//(expected = ForbiddenException.class)
    public void updateProductAsUser() {
        logout();
        login(TEST_CREDENTIALS);
        Product product = target("product")
                .request()
                .post(Entity.json(Collections.singletonMap("productName", "product1")), Product.class);
        target("product")
                .path("productName")
                .path("product1")
                .request()
                .post(Entity.json(null));
        List<Product> products = target("product").request().get(new GenericType<List<Product>>(){});
        assertEquals("product1", products.get(0).getProductName());
    }

    @Test
    public void deleteProduct() {
        Product foo = target("product")
                .request()
                .post(Entity.json(Collections.singletonMap("productName", "product1")), Product.class);
        target("product")
                .path("product1")
                .request()
                .delete();
        List<Product> products = target("product").request().get(new GenericType<List<Product>>(){});
        assertTrue(products.isEmpty());
    }

    @Test //(expected = ForbiddenException.class)
    public void deleteProductAsUser() {
        logout();
        login(TEST_CREDENTIALS);
        Product product = target("product")
                .request()
                .post(Entity.json(Collections.singletonMap("productName", "product1")), Product.class);
        target("product")
                .path("product1")
                .request()
                .delete();
        List<Product> products = target("product").request().get(new GenericType<List<Product>>() {
        });
        assertTrue(products.isEmpty());
    }
}
