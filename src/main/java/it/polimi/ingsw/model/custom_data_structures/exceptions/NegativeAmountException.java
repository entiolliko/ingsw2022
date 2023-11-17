package it.polimi.ingsw.model.custom_data_structures.exceptions;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException() {
        super();
    }

    public NegativeAmountException(String msg) {
        super(msg);
    }
}
