package it.polimi.ingsw.controller.exceptions;

public class LobbyFullException extends ServerException {
    public LobbyFullException() {
        super();
    }

    public LobbyFullException(String message) {
        super(message);
    }
}
