package it.polimi.ingsw.debug_utility;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class DebugLoggerTest {
    @Test
    void correctOutput(){
        DebugLogger.log("PRINT SOMETHING", Level.WARNING);
        DebugLogger.log("PRINT SOMETHING", Level.INFO);
        DebugLogger.log("PRINT SOMETHING", Level.FINEST);
        DebugLogger.log("PRINT SOMETHING", Level.SEVERE);
        DebugLogger.log("PRINT SOMETHING", Level.FINE);
        DebugLogger.log("PRINT SOMETHING", Level.CONFIG);
        DebugLogger.log("PRINT SOMETHING", Level.OFF);
        assertNotNull(DebugLogger.class);
    }

}