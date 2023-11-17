package it.polimi.ingsw.model.custom_data_structures;


import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TokenCollectionTest {
    public TokenCollectionTest() {

    }

    private TokenCollection tested;

    @BeforeEach
    public void setUp() {
        tested = TokenCollection.newEmptyCollection();
    }

    @DisplayName("the constructor shouldn't return null")
    @Test
    void shouldNotBeNull() {
        assertNotNull(TokenCollection.newEmptyCollection());
    }

    @DisplayName("A new TokenCollection should start as empty")
    @Test
    void shouldStartEmpty() {
        assertTrue(TokenCollection.newEmptyCollection().isEmpty());
    }

    @DisplayName("empty should mean that every and each token has a 0 value")
    @Test
    void emptyMeansZeroOfEveryThing() {
        assertTrue(tested.isEmpty());
        for (TokenEnum colour : TokenEnum.values()) {
            assertEquals(0, tested.get(colour));
        }
    }


    @DisplayName("the default bag should have 26 elements of each colour")
    @Test
    void defaultBagShouldHave26fEachColour() {
        tested = TokenCollection.newDefaultBag();
        for (TokenEnum colour : TokenEnum.values()) {
            assertEquals(26, tested.get(colour));
        }

    }

    @DisplayName("createCollection should correctly set the values in the map")
    @ParameterizedTest
    @MethodSource("testGenForCreateCollectionShouldAdhereToTheInput")
    void createCollectionShouldCorrectlySetTheChosenColours(HashMap<TokenEnum, Integer> input) throws NegativeInputException {
        tested = TokenCollection.createCollection(input);
        //selected colours should
        for (TokenEnum colour : input.keySet()) {
            assertEquals(input.get(colour), tested.get(colour));
        }
    }

    @DisplayName("the colours that weren't specified in the map should each have a value of 0")
    @ParameterizedTest
    @MethodSource("testGenForCreateCollectionShouldAdhereToTheInput")
    void createCollectionShouldHave0ForNonSelectedColours(HashMap<TokenEnum, Integer> input) throws NegativeInputException {
        tested = TokenCollection.createCollection(input);
        List<TokenEnum> nonSelectedKeys = Arrays.stream(TokenEnum.values()).filter(colour -> !input.containsKey(colour)).collect(Collectors.toList());
        for (TokenEnum colour : nonSelectedKeys) {
            assertEquals(0, tested.get(colour));
        }
    }

    @DisplayName("if given a null pointer, the createCollection method should throw a NullPointerException")
    @Test
    void createCollectionWorksCorrectlyWithNullInput() {
        assertThrows(NullPointerException.class, () -> TokenCollection.createCollection(null));
    }

    public static Stream<Arguments> testGenForCreateCollectionShouldAdhereToTheInput() {
        List<HashMap<TokenEnum, Integer>> returnValue = new ArrayList<>();
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, 7);
            put(TokenEnum.VIOLET, 15);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, 0);
            put(TokenEnum.VIOLET, 0);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, 0);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, 0);
            put(TokenEnum.VIOLET, 1);
        }});
        returnValue.add(new HashMap<>());
        return returnValue.stream().map(Arguments::of);
    }

    @DisplayName("createCollection should throw a NegativeInputException when any value on the map is negative")
    @ParameterizedTest
    @MethodSource("testGenForCreateCollectionShouldThrowExceptionToAnyNegativeInput")
    void createCollectionShouldThrowExceptionToAnyNegativeInput(HashMap<TokenEnum, Integer> badInput) {
        assertThrows(NegativeInputException.class /*needs to be changed*/, () -> TokenCollection.createCollection(badInput));

    }

    public static Stream<Arguments> testGenForCreateCollectionShouldThrowExceptionToAnyNegativeInput() {
        List<HashMap<TokenEnum, Integer>> returnValue = new ArrayList<>();

        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, -1);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.VIOLET, 0);
            put(TokenEnum.GREEN, -3);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, 1);
            put(TokenEnum.VIOLET, -6);
        }});
        returnValue.add(new HashMap<>() {{
            put(TokenEnum.RED, -1000);
            put(TokenEnum.GREEN, -5000);
        }});
        return returnValue.stream().map(Arguments::of);
    }

    @DisplayName("createCollection should throw a NullPointerException if the key null is present in the map")
    @ParameterizedTest
    @MethodSource("testCasesWithNullKey")
    void createCollectionShouldNotAcceptNullKey(HashMap<TokenEnum, Integer> badInput) {
        assertThrows(NullPointerException.class, () -> TokenCollection.createCollection(badInput));
    }

    @DisplayName("the NullPointerException should precede the NegativeInputException")
    @ParameterizedTest
    @MethodSource("testCasesWithNullKey")
    void NullPointerExceptionShouldPrecedeNegativeInputException(HashMap<TokenEnum, Integer> badInput) {
        assertThrows(NullPointerException.class, () -> TokenCollection.createCollection(badInput));
    }

    public static Stream<Arguments> testCasesWithNullKey() {
        List<HashMap<TokenEnum, Integer>> returnValue = new ArrayList<>();
        returnValue.add(new HashMap<>() {{
            put(null, 0);
        }});
        returnValue.add(new HashMap<>() {{
            put(null, 100);
            put(TokenEnum.GREEN, 10);
        }});
        returnValue.add(new HashMap<>() {{
            put(null, 2);
            put(TokenEnum.GREEN, -7);
        }});
        returnValue.add(new HashMap<>() {{
            put(null, 1);
            put(TokenEnum.RED, 1);
            put(TokenEnum.VIOLET, -1);
        }});
        return returnValue.stream().map(Arguments::of);
    }


    @DisplayName("popToken should reduce the number of tokens by the desired amount")
    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void popTokenShouldReduceByDesiredAmount(TokenEnum colour, int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        tested.popToken(colour, amount);
        assertEquals(26 - amount, tested.get(colour));
    }

    @DisplayName("popToken should fail when the input would throw the amount of tokens below 0")
    @ParameterizedTest
    @CsvSource({"RED, 27", "GREEN, 500", "RED, 8000"})
    void popTokenShouldFailWhenTheInputIsExcessive(TokenEnum colour, int badAmount) {
        tested = TokenCollection.newDefaultBag();
        assertThrows(NegativeAmountException.class, () -> tested.popToken(colour, badAmount));
    }

    @DisplayName("popToken should throw a NegativeInputException when amount is negative")
    @ParameterizedTest
    @CsvSource({"RED, -21", "GREEN, -500", "RED, -8000"})
    void popTokenShouldNotAcceptNegativeInput(TokenEnum colour, int badAmount) {
        TokenCollection test = TokenCollection.newDefaultBag();
        assertThrows(NegativeInputException.class, () -> test.popToken(colour, badAmount));
    }

    @DisplayName("popToken should not accept null as a colour")
    @ParameterizedTest
    @CsvSource({"0", "5", "20"})
    void popTokenShouldNotAcceptNullColour(int amount) {
        TokenCollection test = TokenCollection.newDefaultBag();
        assertThrows(NullPointerException.class, () -> test.popToken(null, amount));
    }

    @DisplayName("popToken should leave the number of the other tokens unchanged")
    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void popTokenShouldLeaveTheOtherColoursUnchanged(TokenEnum colour, int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        tested.popToken(colour, amount);
        List<TokenEnum> leftAloneColours = Arrays.stream(TokenEnum.values()).filter(col -> !col.equals(colour)).collect(Collectors.toList());
        for (TokenEnum col : leftAloneColours) {
            assertEquals(26, tested.get(col));
        }
    }

    @DisplayName("popToken should return a token collection which has only the selected amount of tokens of the chosen colour")
    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void popTokenShouldReturnATokenCollectionWithAmountOfTheSelectedColour(TokenEnum colour, int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag().popToken(colour, amount);
        assertEquals(amount, tested.get(colour));
    }

    @DisplayName("popToken should return a token collection which has only the selected amount of tokens of the chosen colour")
    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void popTokensReturnShouldHaveAllOtherColoursSetTo0(TokenEnum colour, int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag().popToken(colour, amount);
        List<TokenEnum> leftAloneColours = Arrays.stream(TokenEnum.values()).filter(col -> !col.equals(colour)).collect(Collectors.toList());
        for (TokenEnum col : leftAloneColours) {
            assertEquals(0, tested.get(col));
        }
    }

    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void addTokenShouldIncreaseTheSelectedColourCorrectly(TokenEnum colour, int amount) throws NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        int previousSize = tested.get(colour);
        tested.addTokens(colour, amount);
        assertEquals(previousSize + amount, tested.get(colour));

    }

    @ParameterizedTest
    @CsvSource({"RED, 9", "GREEN, 15", "RED, 20", "GREEN, 0"})
    void addTokenShouldLeaveOtherColoursAmountsUnchanged(TokenEnum colour, int amount) throws NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        HashMap<TokenEnum, Integer> previousValues = new HashMap<>();
        for (TokenEnum col : TokenEnum.allBut(colour)) {
            previousValues.put(col, tested.get(col));
        }

        tested.addTokens(colour, amount);

        for (TokenEnum col : TokenEnum.allBut(colour))
            assertEquals(previousValues.get(col), tested.get(col));

    }

    @ParameterizedTest
    @CsvSource({"RED, -9", "GREEN, -15", "RED, -20", "GREEN, -1"})
    void addTokenShouldNotAcceptNegativeInput(TokenEnum colour, int amount) {
        TokenCollection test = TokenCollection.newEmptyCollection();
        assertThrows(NegativeInputException.class, () -> test.addTokens(colour, amount));
    }


    @ParameterizedTest
    @CsvSource({"0", "5", "10", "15", "20"})
    void randomPopReturnShouldHaveTheCorrectSize(int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        tested = tested.randomPop(amount);
        assertEquals(amount, tested.size());
    }

    @ParameterizedTest
    @CsvSource({"0", "5", "10", "15", "20"})
    void randomPopShouldReduceTheTokenCollectionByTheDesiredAmount(int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        int testedOriginalSize = tested.size();
        tested.randomPop(amount);
        assertEquals(testedOriginalSize - amount, tested.size());
    }

    @ParameterizedTest
    @CsvSource({"0", "5", "10", "15", "20"})
    void randomPopShouldLeaveTheTotalSizeUnchanged(int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        int testedOriginalSize = tested.size();
        TokenCollection extracted = tested.randomPop(amount);
        assertEquals(testedOriginalSize, tested.size() + extracted.size());
    }

    @ParameterizedTest
    @CsvSource({"0", "5", "10", "15", "20"})
    void randomPopAndThePoppedCollectionWhenMergedShouldReturnTheOriginal(int amount) throws NegativeAmountException, NegativeInputException {
        tested = TokenCollection.newDefaultBag();
        TokenCollection copy = tested.copy();
        TokenCollection extracted = tested.randomPop(amount);
        tested.addToCollection(extracted);
        assertEquals(copy, tested);
    }

    @ParameterizedTest
    @CsvSource({"131", "200", "100000"})
    void randomPopShouldFailWhenAmountIsBiggerThanItsSize(int badAmount) {
        assertThrows(NegativeAmountException.class, () -> TokenCollection.newDefaultBag().randomPop(badAmount));
    }

    //-----------------
    @ParameterizedTest
    @MethodSource("testCasesForAddCollection")
    void addCollectionShouldCorrectlyIncreaseEveryToken(TokenCollection addedCollection) {
        tested = TokenCollection.newDefaultBag();
        HashMap<TokenEnum, Integer> oldValues = new HashMap<>();
        for (TokenEnum colour : TokenEnum.values())
            oldValues.put(colour, tested.get(colour));
        tested.addToCollection(addedCollection);
        for (TokenEnum colour : TokenEnum.values())
            assertEquals(oldValues.get(colour) + addedCollection.get(colour), tested.get(colour));
    }

    @ParameterizedTest
    @MethodSource("testCasesForAddCollection")
    void addCollectionShouldBeCommutative(TokenCollection addedCollection) {
        tested = TokenCollection.newDefaultBag();
        TokenCollection testedCopy = tested.copy(),
                addedCollectionCopy = addedCollection.copy();

        tested.addToCollection(addedCollection);
        addedCollectionCopy.addToCollection(testedCopy);
        assertEquals(tested, addedCollectionCopy);
    }

    @ParameterizedTest
    @MethodSource("testCasesForAddCollection")
    void addCollectionShouldLeaveItsArgumentUnchanged(TokenCollection addedCollection) {
        TokenCollection copy = addedCollection.copy();
        TokenCollection.newEmptyCollection().addToCollection(addedCollection);
        assertEquals(copy, addedCollection);
    }


    public static Stream<Arguments> testCasesForAddCollection() throws NegativeInputException {
        List<TokenCollection> returnValue = new ArrayList<>();
        returnValue.add(
                TokenCollection.createCollection(new HashMap<>() {{
                    put(TokenEnum.RED, 5);
                    put(TokenEnum.GREEN, 1);
                    put(TokenEnum.YELLOW, 0);
                }})
        );
        returnValue.add(
                TokenCollection.createCollection(new HashMap<>() {{
                    put(TokenEnum.RED, 10);
                }})
        );
        return returnValue.stream().map(Arguments::of);
    }

    @Test
    void copyShouldPassEquivalentObject() {
        tested = TokenCollection.newDefaultBag();
        assertEquals(tested, tested.copy());
    }

    @Test
    void copyShouldNotPassTheSameObject() {
        tested = TokenCollection.newDefaultBag();
        assertNotSame(tested, tested.copy());
    }



}