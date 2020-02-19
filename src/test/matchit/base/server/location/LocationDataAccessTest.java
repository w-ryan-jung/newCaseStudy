package matchit.base.server.location;

import matchit.base.server.Config;
import matchit.base.server.database.BaseDataAccessTest;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Najwa Mekdad, na6363me@student.lu.se
 */
public class LocationDataAccessTest extends BaseDataAccessTest {

    private LocationDataAccess locationDao = new LocationDataAccess(Config.instance().getDatabaseDriver());
   
    @Test
    public void addProduct() {
    	locationDao.addLocation("data1");
         assertTrue(locationDao.deleteLocation("data1"));
    }
    
    @Test
    public void deleteLocation() {
    	 locationDao.addLocation("data1");
         assertTrue(locationDao.deleteLocation("data1"));
    }

    
    @Test
    public void deleteMissingProduct() {
        // No location has been added yet, so the location with name = "data1" should not exist
        String location = "data1";
        assertFalse(locationDao.deleteLocation(location));
    }

    @Test
    public void getProducts() {
        locationDao.addLocation("location1");
        assertEquals(1,  locationDao.getLocations().size());
        locationDao.addLocation("product2");
        assertEquals(2, locationDao.getLocations().size());
        assertEquals(2L, locationDao.getLocations().stream().map(Location::getLocationName).distinct().count());
    }

    @Test
    public void updateProduct() {
        Location location= locationDao.addLocation("location1");
        locationDao.updateLocation("location2");
        assertEquals("location2", locationDao.getLocations().get(0).getLocationName());
    }


}
