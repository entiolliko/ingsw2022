package it.polimi.ingsw.controller.exceptions;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {

    }

    public InvalidCommandException(String msg) {
        super("The command: " + msg + " is incorrect");
    }

    public String toString(String command) {
        return "The command: " + command + " is incorrect";
    }
}
