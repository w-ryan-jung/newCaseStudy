package matchit.base.server.total;

import matchit.base.server.Config;
import matchit.base.server.user.Role;
import matchit.base.server.user.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("total")
public class TotalResource {
    private final TotalDataAccess totalDataAccess = new TotalDataAccess(Config.instance().getDatabaseDriver());

    private final User user;

    public TotalResource(@Context ContainerRequestContext context) {
        this.user = (User) context.getProperty(User.class.getSimpleName());
    }

    @POST
    @RolesAllowed(Role.Names.USER)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Total> getStocksBy(Total item){
        return totalDataAccess.getStocksBy(item);
    }

    @GET
    @Path("location/{location}")
    @RolesAllowed(Role.Names.USER)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Total> getTotalByLocation(@PathParam("location") String location){
        return totalDataAccess.getTotalByLocation(location);
    }

    @GET
    @Path("product/{product}")
    @RolesAllowed(Role.Names.USER)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Total> getTotalByProduct(@PathParam("product") String product){
        return totalDataAccess.getTotalByProduct(product);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public List<Total> getTotal(){
        return totalDataAccess.getTotal();
    }

}
