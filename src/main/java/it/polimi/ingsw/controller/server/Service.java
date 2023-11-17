package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Map;

/**
 * The Service interface is to be implemented by all service providers (i.e.: the Server class and the Lobby class).
 * These classes take client requests and execute them, the methods name are rather self-explanatory.
 * Unless overridden, all methods refuse the request and throw a ServerException
 */
public interface Service {
    default void createLobby(Connection sender, String hostName, int numberOfPlayers, TypeOfGame gameMode) throws ServerException {
        defaultError();
    }

    default void leave(Connection sender) {
    }

    default void joinLobby(Connection sender, String desiredName, String gameId) throws ServerException {
        defaultError();
    }

    default void playApprenticeCard(Connection sender, int cardId) throws ServerException {
        defaultError();
    }

    default void playCharacterCard(Connection sender, String cardName, TokenEnum token, int islandIndex, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove) throws ServerException {
        defaultError();
    }

    default void moveToStudyHall(Connection sender, TokenEnum colour) throws ServerException {
        defaultError();
    }

    default void moveToIsland(Connection sender, TokenEnum colour, int islandIndex) throws ServerException {
        defaultError();
    }

    default void moveMotherNature(Connection sender, int movementValue) throws ServerException {
        defaultError();
    }

    default void pickCloud(Connection sender, int cloudIndex) throws ServerException {
        defaultError();
    }
    default void defaultError() throws ServerException {
        throw new ServerException("you can't do that here");
    }

}
