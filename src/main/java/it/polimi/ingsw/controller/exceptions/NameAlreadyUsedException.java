package it.polimi.ingsw.controller.exceptions;

public class NameAlreadyUsedException extends ServerException {
    public NameAlreadyUsedException(String msg) {
        super(msg);
    }

}
