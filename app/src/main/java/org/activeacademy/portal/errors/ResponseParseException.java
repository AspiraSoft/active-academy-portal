package org.activeacademy.portal.errors;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 02/07/2018 6:38 PM
 */

public class ResponseParseException extends ClassCastException {

    public ResponseParseException() {
        super("Internal server error.");
    }

}
