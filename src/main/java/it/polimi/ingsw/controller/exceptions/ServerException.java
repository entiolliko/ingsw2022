package it.polimi.ingsw.controller.exceptions;

public class ServerException extends Exception {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }
}
