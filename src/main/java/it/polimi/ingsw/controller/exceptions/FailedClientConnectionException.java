package it.polimi.ingsw.controller.exceptions;

import java.io.IOException;

public class FailedClientConnectionException extends RuntimeException {
    public FailedClientConnectionException(IOException e) {
        super("Server was unable to connect to a Client, an IO Exception occurred", e);
    }

}
