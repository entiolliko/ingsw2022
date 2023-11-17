package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReloadGameExceptionTest {
    @Test
    void testExcpetionz(){
        ReloadGameException RE1 = new ReloadGameException(new RuntimeException());
        ReloadGameException RE2 = new ReloadGameException("Test");
        assertEquals(RE2.getMessage(), "Test");
    }

}