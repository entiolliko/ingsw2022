package it.polimi.ingsw.client.frontend.command_line;

import it.polimi.ingsw.client.backend.Connector;
import it.polimi.ingsw.client.backend.DefaultConnector;
import it.polimi.ingsw.client.frontend.PromptSelector;
import it.polimi.ingsw.debug_utility.DebugLogger;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class CommandLineInterface implements Runnable {
    private final Connector connector;
    private final LineInterpreter interpreter;

    public CommandLineInterface() {
        this.connector = new DefaultConnector();

        CLIScreen screen = new CLIScreen();

        PromptSelector promptSelector = new PromptSelector();
        promptSelector.setScreen(screen);
        this.connector.addObserver(promptSelector);

        this.interpreter = new LineInterpreter(connector, System.in, screen);
    }

    public CommandLineInterface(String ip, int port) throws IOException {
        this();
        connector.connectToServer(ip, port);
    }

    public static void main(String[] args) throws IOException {
        if (List.of(args).contains("-debug")) {
            DebugLogger.setLevel(Level.INFO);
        }
        CommandLineInterface subject =  new CommandLineInterface();
        if (List.of(args).contains("-connect")) {
            try {
                int index = List.of(args).indexOf("-connect");
                subject.connector.connectToServer(args[index + 1], Integer.parseInt(args[index + 2]));
            } catch (IOException e) {
                DebugLogger.log("could not connect, try again in the app", Level.SEVERE);
            } catch (RuntimeException e) {
                DebugLogger.log("illegal arguments!", Level.SEVERE);
            }
        }
        subject.run();
    }

    @Override
    public void run() {
        interpreter.startListening();
    }
}
