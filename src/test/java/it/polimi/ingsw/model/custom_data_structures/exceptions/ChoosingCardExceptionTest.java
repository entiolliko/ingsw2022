package it.polimi.ingsw.model.custom_data_structures.exceptions;

import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

class ChoosingCardExceptionTest {
    @Test
    void testing(){
        ChoosingCardException ex = new ChoosingCardException("Test");
        MissingProfessorException ex1 = new MissingProfessorException();
        ex1.getMessage();
        (new PositiveAmountException("")).getMessage();
        (new InvalidCommandException("")).toString("");
    }


}