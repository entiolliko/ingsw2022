package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveToIslandTest {
    PlayerVisitorCommand testCommand;
    Gson serializer;
    Gson deserializer;

    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    MoveToIsland testVisitor;

    @BeforeEach
    void setup() throws ReloadGameException {
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);

        this.testGame.getGameBoard().getDashBoards().get("A").moveToEntranceHall(TokenEnum.GREEN);
        this.testVisitor = new MoveToIsland(TokenEnum.GREEN,2,"A");

        serializer = CustomJsonBuilder.createSerializer();
        deserializer = CustomJsonBuilder.createDeserializer();
        testCommand  = new MoveToIsland(TokenEnum.GREEN, 4, "A");
    }

    @Test
    void setTestCommand() throws ReloadGameException {
        testVisitor.getPlayerID();
        int temp = testGame.getGameBoard().getIslands().getIslandTokens(2).get(TokenEnum.GREEN);
        this.testGame.accept(testVisitor);
        assertTrue(testGame.getGameBoard().getIslands().getIslandTokens(2).get(TokenEnum.GREEN) == temp + 1);
    }

    @Test
    void serializable(){
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }

}