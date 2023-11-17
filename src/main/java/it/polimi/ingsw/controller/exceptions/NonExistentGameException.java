package it.polimi.ingsw.controller.exceptions;

public class NonExistentGameException extends ServerException {
    public NonExistentGameException() {
        super("Requested lobby isn't on disk");
    }

}
