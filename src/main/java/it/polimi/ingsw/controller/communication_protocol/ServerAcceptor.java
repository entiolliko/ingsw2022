package it.polimi.ingsw.controller.communication_protocol;

import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Map;

public interface ServerAcceptor {
    default void accept(ClientRequest request) {
        request.visit(this);
    }

    void createLobby(String hostName, int numberOfPlayers, TypeOfGame gameMode);

    void leave();

    void joinLobby(String desiredName, String gameId);

    void playApprenticeCard(int cardId);

    void playCharacterCard(String cardName, TokenEnum token, int islandIndex, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove);

    void moveToStudyHall(TokenEnum colour);

    void moveToIsland(TokenEnum colour, int islandIndex);

    void moveMotherNature(int movementValue);

    void pickCloud(int cloudIndex);

    void ping();
}
