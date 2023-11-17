package it.polimi.ingsw.controller.exceptions;

public class FailedServerConnectionException extends RuntimeException {
    public FailedServerConnectionException(String s, Exception e) {
        super(s, e);
    }
}
