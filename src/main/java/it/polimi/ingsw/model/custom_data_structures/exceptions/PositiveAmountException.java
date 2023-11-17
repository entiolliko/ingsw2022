package it.polimi.ingsw.model.custom_data_structures.exceptions;

public class PositiveAmountException extends RuntimeException {
    public PositiveAmountException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return "The input amount was to large";
    }
}
