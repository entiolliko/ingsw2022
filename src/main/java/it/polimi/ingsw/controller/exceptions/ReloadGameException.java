package it.polimi.ingsw.controller.exceptions;

public class ReloadGameException extends Exception {
    public ReloadGameException(Exception e) {
        super("reloading last game state", e);
    }

    public ReloadGameException(String msg, Exception e) {
        super(msg, e);
    }

    public ReloadGameException(String msg) {
        super(msg);
    }
}
