package matchit.base.server.product;

import matchit.base.server.Config;
import matchit.base.server.user.Role;
import matchit.base.server.user.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("product")
public class ProductResource {

    private final ProductDataAccess productDataAccess = new ProductDataAccess(Config.instance().getDatabaseDriver());

    private final User user;

    public ProductResource(@Context ContainerRequestContext context) {
        this.user = (User) context.getProperty(User.class.getSimpleName());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public Product addProduct(Product product){
        return productDataAccess.addProduct(product.getProductName(), product.getPrice());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    public List<Product> getProducts(){
        return productDataAccess.getProducts();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    @Path("{product}")
    public boolean updateProduct(@PathParam("product") String product){
        return productDataAccess.updateProduct(product);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RolesAllowed(Role.Names.USER)
    @Path("{product}")
    public boolean deleteProduct(@PathParam("product") String product){
        return productDataAccess.deleteProduct(product);
    }

}
