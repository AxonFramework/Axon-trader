/*
 * Copyright (c) 2010. Gridshore
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.webui.security;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.axonframework.samples.trader.users.api.AuthenticateUserCommand;
import org.axonframework.samples.trader.users.api.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * A custom spring security authentication provider that only supports {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 * authentications. This provider uses Axon's command bus to dispatch an authentication command. The main reason for
 * creating a custom authentication provider is that Spring's UserDetailsService model doesn't fit our authentication
 * model as the UserAccount doesn't hold the password (UserDetailsService expects the UserDetails object to hold the
 * password, which is then compared with the password provided by the {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}.
 *
 * @author Uri Boness
 * @author Jettro Coenradie
 */
@Component
public class TraderAuthenticationProvider implements AuthenticationProvider {
    private final static Collection<GrantedAuthority> userAuthorities;

    static {
        userAuthorities = new HashSet<GrantedAuthority>();
        userAuthorities.add(new GrantedAuthorityImpl("ROLE_USER"));
    }

    @Autowired
    private CommandBus commandBus;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        String password = String.valueOf(token.getCredentials());
        FutureCallback<UserAccount> accountCallback = new FutureCallback<UserAccount>();
        commandBus.dispatch(new AuthenticateUserCommand(username, password.toCharArray()), accountCallback);
        UserAccount account;
        try {
            account = accountCallback.get();
            if (account == null) {
                throw new BadCredentialsException("Invalid username and/or password");
            }
        } catch (InterruptedException e) {
            throw new AuthenticationServiceException("Credentials could not be verified", e);
        } catch (ExecutionException e) {
            throw new AuthenticationServiceException("Credentials could not be verified", e);
        }

        UsernamePasswordAuthenticationToken result =
                new UsernamePasswordAuthenticationToken(account, authentication.getCredentials(), userAuthorities);
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
