package it.polimi.ingsw.model.custom_data_structures;


import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.*;
import java.util.stream.Collectors;

/***
 * <p>This data structure is born with the intent of simplifying all the classes which store tokens inside of them</p>
 * <p>(such as DashBoard, Island and others).</p>
 * <p></p>
 * <p>The underlying principle is actually very simple:</p>
 * <p>An HashMap has the token colours as its keys and each key has an IntegerStack as argument</p>
 */
public class TokenCollection {

    private static final int NUMBER_OF_TOKENS_IN_DEFAULT_BAG = 26;
    private final Map<TokenEnum, IntegerStack> content;

    /***
     * The constructor class is hidden; it only creates an empty hashMap
     */
    //constructor and factories
    private TokenCollection() {
        content = new EnumMap<>(TokenEnum.class);

    }

    /***
     * Returns an empty collection
     * @return a new TokenCollection with zero tokens for each colour
     */
    public static TokenCollection newEmptyCollection() {
        TokenCollection returnValue = new TokenCollection();
        for (TokenEnum colour : TokenEnum.values()) {
            returnValue.content.put(colour, new IntegerStack());
        }
        return returnValue;
    }

    /**
     * <p>Allows the creation of a custom collection.</p>
     * <p>This function's main use is for testing custom TokenCollection on the go</p>
     *
     * @param input <p>A HashMap which contains TokenEnum as keys and Integer as values.</p>
     *              <p>e.g.: {RED: 3, YELLOW : 0, GREEN : 1}.</p>
     *              <p>All unspecified colours are treated as zeroes.</p>
     * @return A collection equivalent to what is described by the passed HashMap
     * @throws NegativeInputException thrown when any value in the map is negative
     * @throws NullPointerException   thrown when the map contains null in its keySet
     */
    public static TokenCollection createCollection(Map<TokenEnum, Integer> input) throws NegativeInputException, NullPointerException {
        if (input.containsKey(null)) throw new NullPointerException();

        TokenCollection returnValue = TokenCollection.newEmptyCollection();
        for (Map.Entry<TokenEnum, Integer> entry : input.entrySet()) {
            returnValue.addTokens(entry.getKey(), entry.getValue());
        }
        return returnValue;
    }

    /***
     * returns a Token collection with 26 tokens for each colour
     * @return a collection of 130 tokens(26 for each colour)
     */
    public static TokenCollection newDefaultBag() {
        TokenCollection returnValue = TokenCollection.newEmptyCollection();
        for (TokenEnum colour : TokenEnum.values()) {
            returnValue.addTokens(colour, NUMBER_OF_TOKENS_IN_DEFAULT_BAG);

        }
        return returnValue;
    }

    //pop methods

    /***
     * this method extracts a certain amount of tokens randomly and returns a TokenCollectionObject
     * @param amount number of tokens to randomly extract
     * @return a collection with "amount" tokens randomly picked from the called TokenCollection object
     * @throws NegativeAmountException  <p>thrown when the desired amount exceeds the quantity of tokens in the collection</p>
     * @throws NegativeInputException   <p>thrown when the input is negative</p>
     */
    public TokenCollection randomPop(int amount) throws NegativeAmountException, NegativeInputException {

        if (amount < 0) throw new NegativeInputException("You cannot pop a negative amount");
        if (this.size() - amount < 0) throw new NegativeAmountException();

        TokenCollection returnValue = TokenCollection.newEmptyCollection();

        for (int i = 0; i < amount; i++) {
            Set<TokenEnum> availableColours = nonEmptyBuckets();
            TokenEnum randomlyChosen = TokenEnum.randomAmong(availableColours);
            returnValue.addToCollection(this.popToken(randomlyChosen, 1));
        }

        return returnValue;
    }

    public TokenEnum randomTokenPop(){
        TokenCollection removed = this.randomPop(1);
        return removed.getMap()
                .entrySet()
                .stream()
                .filter(token -> token.getValue()>0)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(TokenEnum.RED);
    }


    private Set<TokenEnum> nonEmptyBuckets() {
        return this
                .content
                .keySet()
                .stream()
                .filter(colour -> this.get(colour) > 0)
                .collect(Collectors.toSet());
    }


    //add methods

    /**
     * adds n tokens of the desired colour to the called TokenCollection
     *
     * @param colour colour of the tokens to be added
     * @param amount amount of tokens to add
     * @throws NegativeInputException thrown when the input is negative
     */
    public void addTokens(TokenEnum colour, int amount) throws NegativeInputException {
        content.get(colour).add(amount);
    }

    /**
     * tells whether the collection is empty or not
     *
     * @return true if and only if the size of the collection is zero
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    //only getter

    /**
     * returns an integer indicating the amount of tokens of the indicated colour
     *
     * @param colour colour of the tokens whose quantity is asked
     * @return number of tokens of the passed colour
     */
    public int get(TokenEnum colour) {
        return content.get(colour).size();
    }

    /**
     * returns the overall number of tokens in the collection
     *
     * @return overall number of tokens in the collection
     */
    public int size() {
        return content
                .values()
                .stream()
                .mapToInt(IntegerStack::size)
                .sum();
    }

    /***
     * extracts a certain amount of tokens of a certain colour
     * @param colour colour of the tokens to be extracted
     * @param amount amount of tokens to extract
     * @return a Token collection of "amount" tokens of the requested colour
     * @throws NegativeAmountException <p>thrown if the requested amount exceeds the effective amount of tokens</p>
     * <p>of the specified colour in the collection</p>
     * @throws NegativeInputException thrown when input "amount" is negative
     */
    public TokenCollection popToken(TokenEnum colour, int amount) throws NegativeAmountException, NegativeInputException {
        TokenCollection returnValue = TokenCollection.newEmptyCollection();
        content.get(colour).decreaseBy(amount);
        returnValue.addTokens(colour, amount);
        return returnValue;
    }

    /**
     * adds the content of the passed TokenCollection without modifying the passed argument
     *
     * @param toAdd TokenCollection whose content is to be added to the called TokenCollection
     */
    public void addToCollection(TokenCollection toAdd) {
        for (TokenEnum colour : TokenEnum.values()) {
            this.addTokens(colour, toAdd.get(colour));
        }
    }

    /**
     * Removes the content of the passed TokenCollection without modifying the passed argument
     *
     * @param toAdd TokenCollection whose content is to be removed from the called TokenCollection
     */
    public void removeFromCollection(TokenCollection toAdd) {
        for (TokenEnum colour : TokenEnum.values()) {
            this.popToken(colour, toAdd.get(colour));
        }
    }


    /***
     * returns a deep copy of the called TokenCollection
     * @return a deep copy of the called TokenCollection
     */
    public TokenCollection copy() {
        TokenCollection returnValue = new TokenCollection();

        for (Map.Entry<TokenEnum, IntegerStack> entry : this.content.entrySet()) {
            returnValue.content.put(entry.getKey(), new IntegerStack(entry.getValue().size()));
        }
        return returnValue;
    }

    /***
     * default equal method
     * @param o confronted object
     * @return whether the objects are in the same equivalence class or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenCollection that = (TokenCollection) o;
        return Objects.equals(content, that.content);
    }

    public Map<TokenEnum, Integer> getMap() {
        Map<TokenEnum, Integer> returnValue = new HashMap<>();
        for (TokenEnum colour : TokenEnum.values()) {
            int numberOfTokens = this.content.get(colour).size();
            returnValue.put(colour, numberOfTokens);
        }
        return returnValue;
    }

    /***
     * default hashcode method
     * @return hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "TokenCollection{" +
                "content=" + content +
                '}';
    }
}
