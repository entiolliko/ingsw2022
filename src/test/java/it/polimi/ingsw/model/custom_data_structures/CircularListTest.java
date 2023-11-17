package it.polimi.ingsw.model.custom_data_structures;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CircularListTest {
    @ParameterizedTest
    @MethodSource("defaultSuite")
    void removeShouldWorkOnTheEdge(List<Integer> copy) {
        CircularList<Integer> tested = new CircularList<>();
        for(Integer toCopy : copy)
            tested.add(toCopy);

        tested.remove(copy.size());

        assertEquals(copy.get(1), tested.get(0));
    }
    public static Stream<Arguments> defaultSuite() {
        return Stream.of(
                Arguments.of(List.of(0, 1, 2, 3, 4, 5)),
                Arguments.of(List.of(0, 1))
        );
    }


}