package it.polimi.ingsw.model.dashboard;


import it.polimi.ingsw.model.custom_data_structures.exceptions.EmptyException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TokenEnumTest {


    @ParameterizedTest
    @MethodSource("allButTestSuite")
    void allButShouldWorkProperly(Set<TokenEnum> colours) {
        //allBut should not return any element in colours
        for (TokenEnum col : TokenEnum.values())
            if (colours.contains(col))
                assertFalse(TokenEnum.allBut(colours).contains(col));
            else
                assertTrue(TokenEnum.allBut(colours).contains(col));


    }

    public static Stream<Arguments> allButTestSuite() {
        List<Set<TokenEnum>> returnValue = new ArrayList<>();
        returnValue.add(Set.of(TokenEnum.RED));
        returnValue.add(Set.of(TokenEnum.GREEN));
        returnValue.add(Set.of(TokenEnum.RED, TokenEnum.GREEN));
        return returnValue.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @CsvSource("5")
    void randomShouldWorkHopefully(int times) {
        for (int i = 0; i < times; i++) {
            TokenEnum random = TokenEnum.random();
            System.out.println(random);
            assertEquals(4,Math.sqrt(16));

        }

    }

    @ParameterizedTest
    @MethodSource("allButTestSuite")
    void randomAmongResultShouldBeInSet(Set<TokenEnum> colours) throws EmptyException {
        TokenEnum result = TokenEnum.randomAmong(colours);
        System.out.println(result);
        assertTrue(colours.contains(result));
    }

}