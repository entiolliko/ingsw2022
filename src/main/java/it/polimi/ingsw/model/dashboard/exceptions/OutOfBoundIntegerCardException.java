package it.polimi.ingsw.model.dashboard.exceptions;

public class OutOfBoundIntegerCardException extends Exception {
    public OutOfBoundIntegerCardException() {
        super("Integer provided was not in card boundaries");
    }

}
