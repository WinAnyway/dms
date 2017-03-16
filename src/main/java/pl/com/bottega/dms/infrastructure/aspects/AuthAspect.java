package pl.com.bottega.dms.infrastructure.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import pl.com.bottega.dms.application.user.AuthRequiredException;
import pl.com.bottega.dms.application.user.CurrentUser;
import pl.com.bottega.dms.application.user.RequiresAuth;

import java.util.Set;

@Component
@Aspect
public class AuthAspect {

    private CurrentUser currentUser;

    public AuthAspect(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Before(value = "@within(requiresAuth)")
    public void ensureAuthForClasses(RequiresAuth requiresAuth) {
        checkIfUserIsLoggedIn();
        checkIfUserHasProperRoles(requiresAuth);

    }

    @Before(value = "@annotation(requiresAuth)")
    public void ensureAuthForMethods(RequiresAuth requiresAuth) {
        checkIfUserIsLoggedIn();
        checkIfUserHasProperRoles(requiresAuth);

    }

    private void checkIfUserIsLoggedIn() {
        if (currentUser.getEmployeeId() == null)
            throw new AuthRequiredException("You need to be logged in for this action");
    }

    private void checkIfUserHasProperRoles(RequiresAuth requiresAuth) {
        String[] roles = requiresAuth.roles();
        Set<String> currentUserRoles = currentUser.getRoles();

        for (String role : roles) {
            if (!currentUserRoles.contains(role)) {
                throw new AuthRequiredException("User lacks priveleges for this action");
            }
        }
    }

}
