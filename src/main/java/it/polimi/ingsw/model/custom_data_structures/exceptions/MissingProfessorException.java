package it.polimi.ingsw.model.custom_data_structures.exceptions;

public class MissingProfessorException extends RuntimeException {
    @Override
    public String getMessage() {
        return "The professor is missing";
    }
}
