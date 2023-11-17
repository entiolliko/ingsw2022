package it.polimi.ingsw.controller.utility;


public class ProgressiveNumberGenerator {
    private int currentNumber;

    /**
     * creates a progressive number generator, whose starting value is the one passed into the constructor
     * @param initialNumber starting value
     */
    public ProgressiveNumberGenerator(int initialNumber) {
        this.currentNumber = initialNumber;
    }

    /**
     * returns the next value
     * @return next value
     */
    public synchronized int nextValue() {
        return currentNumber++;
    }
}
