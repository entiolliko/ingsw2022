package it.polimi.ingsw.model.dashboard.exceptions;

public class AlreadyPlayedCardException extends Exception {

    public AlreadyPlayedCardException(String errorMessage) {
        super(errorMessage);
    }
}
