package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.islands.IslandChain;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveMotherNatureTest extends IslandChain {
    List<String> playersID;
    List<String> magicians;
    List<String> squads;
    String gameID;
    Game testGame;
    Gson serializer;
    Gson deserializer;
    @BeforeEach
    void setUp() {
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game0";
        testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
    }

    @ParameterizedTest
    @CsvSource({"A, 1, 1", "B, 10, 1", "C, 2, 5"})
    void moveMotherNatureShouldMoveAccordingly(String player, int movementAmount, int cardIndex) throws ReloadGameException {
        testGame.accept(new PlayApprenticeCard(player, cardIndex)); // a card ought to be played before movement command can be issued
        testGame.accept(new MoveMotherNature(player, movementAmount));


        int apprenticeCardMovementAmount = testGame.getGameBoard().getCardHandler().lastPlayedCardMovement(player);
        int motherNaturePos = testGame.getGameBoard().getIslands().getCurrMotherNaturePos();
        assertEquals(Math.min(movementAmount, apprenticeCardMovementAmount), motherNaturePos);
    }
    @ParameterizedTest
    @CsvSource({"A, 1, 1", "B, 10, 1", "C, 2, 5", "A, 6, 8", "B, 7, 8"})
    void moveMotherNatureShouldWortWithBonus(String player, int movementAmount, int cardIndex) throws ReloadGameException {
        testGame.accept(new PlayApprenticeCard(player, cardIndex));
        testGame
                .getGameBoard()
                .getCardHandler()
                .setBonusReceiver(player);
        testGame.accept(new MoveMotherNature(player, movementAmount));

        int maximumAllowedMovement = testGame.getGameBoard().getCardHandler().lastPlayedCardMovement(player);
        int motherNaturePos = testGame.getGameBoard().getIslands().getCurrMotherNaturePos();
        assertEquals(Math.min(movementAmount, maximumAllowedMovement), motherNaturePos);
    }
    @ParameterizedTest
    @CsvSource({"A, 0", "A, 1", "A, 12", "A, 15", "B, 5", "B, 0"})
    void moveMotherNatureShouldFailIfNoCardHasBeenPlayed(String player, int movementAmount) {
        VisitorCommand moveMotherNatureCommand = new MoveMotherNature(player, movementAmount);
        assertThrows(ReloadGameException.class, () -> testGame.accept(moveMotherNatureCommand));
    }

    @Test
    void serializable(){
        PlayerVisitorCommand testCommand = new MoveMotherNature("A", 4);
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }
}