package it.polimi.ingsw.controller.exceptions;

import java.io.IOException;

public class FailedServerSocketCreationException extends RuntimeException {
    public FailedServerSocketCreationException(Integer port, IOException e) {
        super("serverSocket was unable to create, port:" + port + " could be already in use", e);
    }
}
