package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.utility.ProgressiveNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ProgressiveNumberGeneratorTest {
    ProgressiveNumberGenerator testGen;
    int startingValue;
    @BeforeEach
    void setup(){
        startingValue = new Random().nextInt(100);
        this.testGen = new ProgressiveNumberGenerator(startingValue);
    }

    @Test
     void correctSeries(){
        assertNotNull(testGen);
        assertEquals(startingValue, testGen.nextValue());
        assertNotEquals(testGen.nextValue(), testGen.nextValue());
    }

}