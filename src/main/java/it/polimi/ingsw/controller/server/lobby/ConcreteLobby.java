package it.polimi.ingsw.controller.server.lobby;

import it.polimi.ingsw.controller.exceptions.InvalidArgsException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.visitor.character_cards.PlayCharacterCardCommand;
import it.polimi.ingsw.model.visitor.player_visitor_command.*;

import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the Lobby abstract class that cares to implement the methods in the Service interface which
 * need to be overridden
 */
public class ConcreteLobby extends Lobby {
    public ConcreteLobby(String gameID, int numberOfPlayers, TypeOfGame gameMode) throws InvalidArgsException {
        super(gameID, numberOfPlayers, gameMode);
    }

    public ConcreteLobby(String gameID) {
        super(gameID);
    }

    @Override
    public void leave(Connection sender) {
        this.onRemovePlayer(sender);
    }

    @Override
    public void playApprenticeCard(Connection sender, int cardId) throws ServerException {
        PlayerVisitorCommand command = new PlayApprenticeCard(sender.getName(), cardId);
        super.onExecuteCommand(command);
    }

    @Override
    public void playCharacterCard(Connection sender, String cardName, TokenEnum token, int islandIndex, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove) throws ServerException {
        TokenCollection toAddTC = Objects.isNull(toAdd)?null:TokenCollection.createCollection(toAdd);
        TokenCollection toRemoveTC = Objects.isNull(toRemove)?null:TokenCollection.createCollection(toRemove);
        PlayerVisitorCommand command = new PlayCharacterCardCommand(cardName, sender.getName(), token, islandIndex, toAddTC, toRemoveTC);
        super.onExecuteCommand(command);
    }

    @Override
    public void moveToStudyHall(Connection sender, TokenEnum colour) throws ServerException {
        PlayerVisitorCommand command = new MoveToStudyHall(sender.getName(), colour);
        super.onExecuteCommand(command);
    }

    @Override
    public void moveToIsland(Connection sender, TokenEnum colour, int islandIndex) throws ServerException {
        PlayerVisitorCommand command = new MoveToIsland(colour, islandIndex, sender.getName());
        super.onExecuteCommand(command);
    }

    @Override
    public void moveMotherNature(Connection sender, int movementValue) throws ServerException {
        PlayerVisitorCommand command = new MoveMotherNature(sender.getName(), movementValue);
        super.onExecuteCommand(command);
    }

    @Override
    public void pickCloud(Connection sender, int cloudIndex) throws ServerException {
        PlayerVisitorCommand command = new PickCloud(sender.getName(), cloudIndex);
        super.onExecuteCommand(command);
    }
}
