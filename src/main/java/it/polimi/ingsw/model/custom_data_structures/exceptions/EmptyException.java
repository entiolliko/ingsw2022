package it.polimi.ingsw.model.custom_data_structures.exceptions;

public class EmptyException extends RuntimeException {
    public EmptyException(String errMsg) {
        super(errMsg);
    }
    public EmptyException() {
        super();
    }
}
