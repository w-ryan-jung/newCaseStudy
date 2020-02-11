package matchit.base.server.balance;

import matchit.base.server.Config;
import matchit.base.server.user.Role;
import matchit.base.server.user.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("total/")
public class BalanceResource {
    private final BalanceDataAccess balanceDataAccess = new BalanceDataAccess(Config.instance().getDatabaseDriver());

    private final User user;

    public BalanceResource(@Context ContainerRequestContext context) {
        this.user = (User) context.getProperty(User.class.getSimpleName());
    }

    @GET
    @Path("location/{location}")
    @RolesAllowed(Role.Names.USER)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Balance> getTotalByLocation(@PathParam("location") String location){
        System.out.println(location);
        return balanceDataAccess.getTotalByLocation(location);
    }

    @GET
    @Path("product/{product}")
    @RolesAllowed(Role.Names.USER)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Balance> getTotalByProduct(@PathParam("product") String product){
        return balanceDataAccess.getTotalByProduct(product);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public List<Balance> getTotal(){
        return balanceDataAccess.getTotal();
    }

}
