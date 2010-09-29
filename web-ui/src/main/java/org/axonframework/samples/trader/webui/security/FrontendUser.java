package org.axonframework.samples.trader.webui.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author Jettro Coenradie
 */
public class FrontendUser extends User {
    private String longName;

    public FrontendUser(String username, String password, String longName,
                        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.longName = longName;
    }

    public String getLongName() {
        return longName;
    }
}
