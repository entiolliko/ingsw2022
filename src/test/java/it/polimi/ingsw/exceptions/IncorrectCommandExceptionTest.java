package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

class IncorrectCommandExceptionTest {
    @Test
    void testMethod(){
        InvalidCommandException cm = new InvalidCommandException();
        InvalidCommandException cm1 = new InvalidCommandException("Test");
        String testt = cm.toString();
    }
}