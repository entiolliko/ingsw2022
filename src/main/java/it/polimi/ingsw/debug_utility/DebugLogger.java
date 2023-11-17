package it.polimi.ingsw.debug_utility;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugLogger {
    private static final Logger instance;

    static {
        Logger returnValue = Logger.getLogger("log.txt");
        returnValue.setLevel(Level.SEVERE);
        instance = returnValue;
    }

    private DebugLogger() {
    }

    public static void log(String message, Level severity) {
        instance.log(severity, message);
    }
    public static void setLevel(Level level){
        instance.setLevel(level);
    }

}
