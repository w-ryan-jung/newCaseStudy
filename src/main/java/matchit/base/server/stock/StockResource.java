package matchit.base.server.stock;

import matchit.base.server.Config;
import matchit.base.server.user.Role;
import matchit.base.server.user.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("stock")
public class StockResource {

    private final StockDataAccess stockDataAccess = new StockDataAccess(Config.instance().getDatabaseDriver());

    private final User user;

    public StockResource(@Context ContainerRequestContext context) {
        this.user = (User) context.getProperty(User.class.getSimpleName());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public Stock addStock(Stock stock){
        return stockDataAccess.addStock(user.getId(), stock.getLocationName(), stock.getProductName(), stock.getAmount());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public List<Stock> getStocks(){
        return stockDataAccess.getUserStock(user.getId());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.ADMIN)
    @Path("user/{userId}")
    public List<Stock> getUserStocks(@PathParam("userId") int userId){
        return stockDataAccess.getUserStock(userId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.ADMIN)
    @Path("all")
    public List<Stock> getAllStock(){
        return stockDataAccess.getAllStock();
    }



}
