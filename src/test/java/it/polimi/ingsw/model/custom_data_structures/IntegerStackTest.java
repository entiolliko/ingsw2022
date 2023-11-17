package it.polimi.ingsw.model.custom_data_structures;

import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;


import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class IntegerStackTest {
    private IntegerStack tested;

    @BeforeEach
    public void setUp() throws NegativeInputException {
        tested = new IntegerStack();
    }

    @DisplayName("constructor shouldn't return null")
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "100000"})
    void shouldNotBeNull(int startingAmount) throws NegativeInputException {
        assertNotNull(new IntegerStack(startingAmount));
    }

    @Test
    @DisplayName("should be Empty when called the 0 arguments empty constructor")
    void shouldBeEmptyAtStart() {
        assertTrue(tested.isEmpty());
        assertEquals(0, tested.size());
    }

    @DisplayName("constructor should work correctly with non-negative starting amounts")
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4"})
    void startUpShouldWorkCorrectlyWithPositiveAmounts(int startingAmount) throws NegativeInputException {

        assertEquals(startingAmount, new IntegerStack(startingAmount).size());
    }

    @DisplayName("constructor should fail with negative starting amounts")
    @ParameterizedTest
    @CsvSource({"-1", "-2", "-3", "-4"})
    void startUpShouldThrowExceptionWithNegativeAmounts(int startingAmount) {
        assertThrows(NegativeInputException.class, () -> new IntegerStack(startingAmount));
    }

    @DisplayName("decrease should reduce size by desired amount")
    @ParameterizedTest
    @CsvSource({"0,0", "1, 0", "1, 1", "3, 2", "4, 1", "100000, 1234", "41, 41"})
    void decreaseByShouldDecreaseByDesiredAmount(int startingAmount, int toReduce) throws NegativeAmountException, NegativeInputException {
        tested = new IntegerStack(startingAmount);
        tested.decreaseBy(toReduce);

        assertEquals(startingAmount - toReduce, tested.size());
    }
    @DisplayName("decrease corner case")
    @ParameterizedTest
    @CsvSource({"0, 0", "1, 1", "10, 10", "100, 1000"})
    void decreaseAmountShouldWorkFineWithCornerCase(int amount) throws NegativeAmountException {
        tested = new IntegerStack(amount) {{decreaseBy(amount);}};
        assertEquals(0, tested.size());
    }

    @DisplayName("decreaseBy should fail when it throws the amount below 0")
    @ParameterizedTest
    @CsvSource({"0, 1", "1, 2", "100, 100000", "3, 4", "7, 50"})
    void decreaseByShouldThrowExceptionIfAmountFallsBelowZero(int startingAmount, int toReduce) {
        assertThrows(NegativeAmountException.class, () -> new IntegerStack(startingAmount).decreaseBy(toReduce));
    }

    @DisplayName("decreaseBy should fail when its input is negative")
    @ParameterizedTest
    @CsvSource({"0, -1", "1 , -1", "7, -2", "5000, -2"})
    void decreaseByShouldFailWhenPassedNegativeInput(int startingAmount, int toReduce) {
        IntegerStack test = new IntegerStack(startingAmount);
        assertThrows(NegativeInputException.class, () -> test.decreaseBy(toReduce));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "0, 1", "1, 2", "9, 12"})
    void addShouldIncreaseByDesiredAmount(int startingAmount, int toIncrease) throws NegativeInputException {
        tested = new IntegerStack(startingAmount);
        tested.add(toIncrease);
        assertEquals(startingAmount + toIncrease, tested.size());
    }

    //insertedValuesOughtToBeNegativeForThisTest
    @ParameterizedTest
    @CsvSource({"0, -1", "2, -3", "10, -10", "7, -2"})
    void addShouldNotAllowNegativeNumbers(int startingAmount, int toIncrease) throws NegativeInputException {
        tested = new IntegerStack(startingAmount);
        assertThrows(NegativeInputException.class, () -> tested.add(toIncrease));
    }

    @ParameterizedTest
    @CsvSource({"0, 7", "7, 0", "0, 0", "1, 1", "2, 1", "1, 2"})
    void injectShouldEnlargeTheCallerCorrectly(int firstStack, int secondStack) throws NegativeInputException {
        tested = new IntegerStack(firstStack);
        tested.inject(new IntegerStack(secondStack));
        assertEquals(firstStack + secondStack, tested.size());
    }
    @ParameterizedTest
    @CsvSource({"0, 7", "7, 0", "0, 0", "1, 1", "2, 1", "1, 2"})
    void injectShouldBeCommutative(int firstStack, int secondStack) throws NegativeInputException {
        IntegerStack stack1 = new IntegerStack(firstStack);
        stack1.inject(new IntegerStack(secondStack));

        IntegerStack stack2 = new IntegerStack(secondStack);
        stack2.inject(new IntegerStack(firstStack));

        assertEquals(stack1.size(), stack2.size());

    }

}
