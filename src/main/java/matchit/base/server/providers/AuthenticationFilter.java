package matchit.base.server.providers;

import matchit.base.server.database.ErrorType;
import matchit.base.server.Config;
import matchit.base.server.user.Session;
import matchit.base.server.user.User;
import matchit.base.server.user.UserDataAccess;
import matchit.base.server.database.DataAccessException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

/**
 * Adds the Session to the current request. This is done by extracting the token in the users cookie and
 * checking the database for the cookie.
 *
 * @author Rasmus Ros, rasmus.ros@cs.lth.se
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class AuthenticationFilter implements ContainerRequestFilter {

    private final UserDataAccess userDataAccess = new UserDataAccess(Config.instance().getDatabaseDriver());

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Cookie cookie = requestContext.getCookies().get("USER_TOKEN");
        Session session = new Session(null, User.NONE);
        if (cookie != null) {
            UUID uuid = UUID.fromString(cookie.getValue());
            try {
                session = userDataAccess.getSession(uuid);
            } catch (DataAccessException e) {
                if (e.getErrorType() != ErrorType.NOT_FOUND) {
                    throw e;
                }
            }
        }
        requestContext.setProperty(Session.class.getSimpleName(), session);
        requestContext.setProperty(User.class.getSimpleName(), session.getUser());
        requestContext.setSecurityContext(session);
    }
}
