package org.activeacademy.portal.errors;

import javax.security.auth.login.LoginException;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 02/07/2018 6:37 PM
 */

public class AuthenticationException extends LoginException {

    public AuthenticationException() {
        super("User not authenticated.");
    }

}
