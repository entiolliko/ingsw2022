package it.polimi.ingsw.model.islands;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureEnumTest {

    @ParameterizedTest
    @CsvSource({"PRESENT, PRESENT, PRESENT",
                "PRESENT, ABSENT, PRESENT",
                "PRESENT, TEMP, PRESENT",
                "ABSENT, PRESENT, PRESENT",
                "ABSENT, ABSENT, ABSENT",
                "ABSENT, TEMP, TEMP",
                "TEMP, PRESENT, PRESENT",
                "TEMP, ABSENT, TEMP",
                "TEMP, TEMP, TEMP"
                })
    void maxShouldReturnCorrectValue(MotherNatureEnum a, MotherNatureEnum b, MotherNatureEnum expected) {
        assertEquals(expected, MotherNatureEnum.max(a, b));
    }

}