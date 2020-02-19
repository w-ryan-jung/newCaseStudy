package matchit.base.server.location;

import matchit.base.server.BaseResourceTest;
import matchit.base.server.Config;
import matchit.base.server.product.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class LocationResourceTest extends BaseResourceTest {
	 private static final GenericType<List<Location>> LOCATION_LIST = new GenericType<List<Location>>() {
    };
	private final LocationDataAccess locationDao = new LocationDataAccess(Config.instance().getDatabaseDriver());
        
    @Before
    public void loginAdmin() {
        login(ADMIN_CREDENTIALS);
    }

    @Test
    public void addLocation() {
    	  Location location = target("location").request()
                 .post(Entity.json(Collections.singletonMap("locationName", "location1")), Location.class);
        assertEquals("location1", location.getLocationName());

       }

    @Test// (expected = ForbiddenException.class)
    public void addLocationAsUser() {
    	logout();
    	login(TEST_CREDENTIALS);
    	 Location location = target("location").request()
                 .post(Entity.json(Collections.singletonMap("locationName", "location1")), Location.class);
        assertEquals("location1", location.getLocationName());


    }

    @Test
    public void addManyLocation() {
        for (int i = 0; i < 10; i++) {
        	Location location = target("location").request()
                    .post(Entity.json(Collections.singletonMap("locationName", (Integer.toString(i + 1)))), Location.class);
           assertEquals(Integer.toString(i +1), location.getLocationName());  
        }
         
    }
    @Test
    public void getNoLocationData() {
        List<Location> list = target("location").request().get(LOCATION_LIST);
        assertTrue(list.isEmpty());
    }

    @Test
    public void updateLocation() {
        Location location = target("location")
                .request()
                .post(Entity.json(Collections.singletonMap("locationName", "location1")), Location.class);
        target("location")
                .path("locationName")
                .path("location1")
                .request()
                .post(Entity.json(null));
        List<Location> locations = target("location").request().get(new GenericType<List<Location>>(){});
        assertEquals("location1", locations.get(0).getLocationName());
    }

    @Test//(expected = ForbiddenException.class)
    public void updateLocationAsUser() {
        logout();
        login(TEST_CREDENTIALS);
        Location location = target("location")
                .request()
                .post(Entity.json(Collections.singletonMap("locationName", "location1")), Location.class);
        target("location")
                .path("locationName")
                .path("location1")
                .request()
                .post(Entity.json(null));
        List<Location> locations = target("location").request().get(new GenericType<List<Location>>(){});
        assertEquals("location1", locations.get(0).getLocationName());
    }

    @Test
    public void deleteLocation() {
        Product foo = target("location")
                .request()
                .post(Entity.json(Collections.singletonMap("locationName", "location1")), Product.class);
        target("location")
                .path("location1")
                .request()
                .delete();
        List<Product> products = target("location").request().get(new GenericType<List<Product>>(){});
        Assert.assertTrue(products.isEmpty());
    }

    @Test //(expected = ForbiddenException.class)
    public void deleteProductAsUser() {
        logout();
        login(TEST_CREDENTIALS);
        Location location = target("location")
                .request()
                .post(Entity.json(Collections.singletonMap("locationName", "location1")), Location.class);
        target("location")
                .path("location1")
                .request()
                .delete();
    }
}
