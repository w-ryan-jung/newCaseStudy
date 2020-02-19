package matchit.base.server;

import matchit.base.server.total.Total;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TotalResourceTest extends BaseResourceTest {

    private static final GenericType<List<Total>> TOTAL_LIST = new GenericType<List<Total>>() {
    };

    @Before
    public void loginTest() {
        login(TEST_CREDENTIALS);
    }

    @Test(expected = ForbiddenException.class)
    public void getTotalAsNoOne() {
        logout();
        target("total").request().get(TOTAL_LIST);
    }
    
//    @Test (expected = ForbiddenException.class)
//    public void addProductAsUser() {
//    	logout();
//    	login(TEST_CREDENTIALS);
//        Product product = target("product").request()
//                .post(Entity.json(Collections.singletonMap("price", 5000)), Product.class);
//    }

    @Test
    public void getNoTotalData() {
        List<Total> list = target("total").request().get(TOTAL_LIST);
        assertTrue(list.isEmpty());
    }
}
