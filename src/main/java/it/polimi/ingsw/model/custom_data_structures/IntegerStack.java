package it.polimi.ingsw.model.custom_data_structures;

import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;

import java.util.Objects;

/***
 * This class contains an integer that can be incremented by any amount and likewise be reduced, on the condition its value never falls below zero
 */
public class IntegerStack {

    private int amount;

    /***
     *
     * @param amount is the initial amount we want the IntegerStack to have
     * @throws NegativeInputException is thrown when the starting amount would be negative, thus creating a negative amount integer
     */
    public IntegerStack(int amount) throws NegativeInputException {
        if (amount < 0) throw new NegativeInputException("The amount must be positive");
        this.amount = amount;
    }

    /***
     * same as the default constructor but the given amount is 0 by default
     * @throws NegativeInputException is present so that the signature for both te constructors is identical
     */
    public IntegerStack() throws NegativeInputException {
        this(0);
    }

    /***
     * used to check whether the IntegerStack is empty or not
     * @return the truth value of IntegerStack's amount being 0
     */
    public boolean isEmpty() {
        return amount == 0;
    }

    /***
     * used to get the size of the IntegerStack
     * @return the value of amount in IntegerStack
     */
    public int size() {
        return amount;
    }

    /***
     * increases the "amount" value of the called object by the desired amount, as long as such a value is non-negative
     * @param amountToAdd the quantity the caller wants to Increase the IntegerStack amount attribute by
     * @throws NegativeInputException thrown when the input value is below zero
     */
    public void add(int amountToAdd) throws NegativeInputException {
        if (amountToAdd < 0) throw new NegativeInputException("You cannot add a negative amount");
        this.amount += amountToAdd;
    }

    /***
     * the exact opposite of the "add" method
     * @param toReduce the amount IntegerStack's amount has to be reduced by
     * @throws NegativeAmountException  thrown when the toReduce value would make the IntegerStack amount attribute fall below zero
     * @throws NegativeInputException  thrown when the input value is below zero
     */
    public void decreaseBy(int toReduce) throws NegativeAmountException, NegativeInputException {
        if (toReduce < 0) throw new NegativeInputException("you cannot take a negative amount of  tokens, dummy");
        if (this.amount - toReduce < 0) throw new NegativeAmountException("you cannot take tokens that do no exist");
        this.amount -= toReduce;
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
        IntegerStack that = (IntegerStack) o;
        return amount == that.amount;
    }

    @Override
    public String toString() {
        return "[" + amount + "]";
    }

    /***
     * default hashcode method
     * @return hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /***
     * <p>the amount value of the called object is increased by the argument's amount value</p>
     * <p>the argument is left unchanged</p>
     * @param toInject IntegerStack whose content are to be injected, this object is unaffected by the method call
     */
    public void inject(IntegerStack toInject) {
        this.add(toInject.size());
    }
}
