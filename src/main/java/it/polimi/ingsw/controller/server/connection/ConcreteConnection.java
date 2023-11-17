package it.polimi.ingsw.controller.server.connection;

import it.polimi.ingsw.controller.communication_protocol.server_responses.ErrorRes;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.Service;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * This class is an implementation of the Connection abstract class that cares to implement
 * all the ServerAcceptor interface methods
 * All the below methods do is call the correct method to the connection service provider
 * Notable exceptions are the leave request, which severs the connection AND THEN calls the service provider method
 * and the ping message, which just refreshes the PingTimer countdown
 */
public class ConcreteConnection extends Connection {

    public ConcreteConnection(Socket socket, Service service) throws IOException {
        super(socket, service);
    }

    @Override
    public void createLobby(String hostName, int numberOfPlayers, TypeOfGame gameMode) {
        try {
            getService().createLobby(this, hostName, numberOfPlayers, gameMode);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }

    }

    @Override
    public void leave() {
        getService().leave(this);
        disconnect();
    }

    @Override
    public void joinLobby(String desiredName, String gameId) {
        try {
            getService().joinLobby(this, desiredName, gameId);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void playApprenticeCard(int cardId) {
        try {
            getService().playApprenticeCard(this, cardId);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void playCharacterCard(String cardName, TokenEnum token, int islandIndex, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove) {
        try {
            getService().playCharacterCard(this, cardName, token, islandIndex, toAdd, toRemove);
        }catch (ServerException e){
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void moveToStudyHall(TokenEnum colour) {
        try {
            getService().moveToStudyHall(this, colour);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void moveToIsland(TokenEnum colour, int islandIndex) {
        try {
            getService().moveToIsland(this, colour, islandIndex);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void moveMotherNature(int movementValue) {
        try {
            getService().moveMotherNature(this, movementValue);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void pickCloud(int cloudIndex) {
        try {
            getService().pickCloud(this, cloudIndex);
        } catch (ServerException e) {
            send(new ErrorRes(e.getMessage()));
        }
    }

    @Override
    public void ping() {
        timer.refresh();
    }

}
