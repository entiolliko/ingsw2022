package it.polimi.ingsw.client.backend;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class Connector{
    private final Set<ClientAcceptor> observers;

    protected Connector() {
        this.observers = new HashSet<>();
    }

    public void addObserver(ClientAcceptor observer) {
        this.observers.add(observer);
    }

    public void removeObserver(ClientAcceptor observer) {
        this.observers.remove(observer);
    }

    /**
     * connects to server
     * @param ip : server ip
     * @param port : server port
     * @throws IOException
     */
    public abstract void connectToServer(String ip, int port) throws IOException;

    /**
     * disconnects from server
     */
    public abstract void disconnect();

    /**
     * receives a successful disconnection response
     * @param response : a received ServerResponse
     */
    public abstract void disconnect(ServerResponse response);

    /**
     * informs the server that the client is severing the connection
     */
    public abstract void quit();

    /**
     * updates all observers with specified ServerResponse
     * @param message  : a received ServerResponse
     */
    public void updateAll(ServerResponse message) {
        for (ClientAcceptor observer : observers) {
            observer.accept(message);
        }
    }

    /**
     * sends a ClientRequest to the Server
     * @param request : a Client Request to send
     */
    public abstract void sendRequest(ClientRequest request);
}
