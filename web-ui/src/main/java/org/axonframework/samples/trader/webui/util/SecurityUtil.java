package org.axonframework.samples.trader.webui.util;

import org.axonframework.samples.trader.app.api.user.UserAccount;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Jettro Coenradie
 */
public class SecurityUtil {
    public static String obtainLoggedinUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserAccount) {
            return ((UserAccount) principal).getUserName();
        } else {
            throw new IllegalStateException("Wrong security implementation, expecting a UserAccount as principal");
        }
    }
}
