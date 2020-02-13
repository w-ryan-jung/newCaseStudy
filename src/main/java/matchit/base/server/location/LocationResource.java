package matchit.base.server.location;

import matchit.base.server.Config;
import matchit.base.server.user.Role;
import matchit.base.server.user.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("location")
public class LocationResource {
    private final LocationDataAccess locationDataAccess = new LocationDataAccess(Config.instance().getDatabaseDriver());

    private final User user;

    public LocationResource(@Context ContainerRequestContext context) {
        this.user = (User) context.getProperty(User.class.getSimpleName());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public Location addLocation(Location location){
        return locationDataAccess.addLocation(location.getLocationName());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public List<Location> getLocations(){
        return locationDataAccess.getLocations();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    @Path("{location}")
    public boolean updateLocation(@PathParam("location") String location){
        return locationDataAccess.updateLocation(location);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    @Path("{location}")
    public boolean deleteLocation(@PathParam("location") String location){
        return locationDataAccess.deleteLocation(location);
    }
}
