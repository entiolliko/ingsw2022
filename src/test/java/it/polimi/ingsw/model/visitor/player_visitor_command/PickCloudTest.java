package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PickCloudTest {

    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    private PickCloud testVisitor;
    private PlayerVisitorCommand testCommand;
    private Gson serializer;
    private Gson deserializer;

    @BeforeEach
    void reset(){

        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        this.testVisitor = new PickCloud("A", 1);
        this.testCommand = new PickCloud("A", 1);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();

    }

    @Test
    void testAddCloud(){
        testGame.getGameBoard().getDashBoards().get("A").removeFromEntranceHall(testGame.getGameBoard().getDashBoards().get("A").cloneEntranceHall());
        testVisitor.getPlayerID();
        try{
            this.testGame.accept(testVisitor);
        }
        catch (Exception e){
        }
        assertNotEquals(this.testGame.getGameBoard().getClouds().get(0), new Cloud(0));
        assertEquals(this.testGame.getGameBoard().getClouds().get(1).removeTokens(), TokenCollection.newEmptyCollection());
        assertNotEquals(this.testGame.getGameBoard().getClouds().get(2), new Cloud(0));
        assertNotEquals(this.testGame.getGameBoard().getClouds().get(3), new Cloud(0));

        assertNotEquals(this.testGame.getGameBoard().getDashBoards().get("A").cloneEntranceHall(), TokenCollection.newEmptyCollection());
        assertNotEquals(this.testGame.getGameBoard().getDashBoards().get("B").cloneEntranceHall(), TokenCollection.newEmptyCollection());
        assertNotEquals(this.testGame.getGameBoard().getDashBoards().get("C").cloneEntranceHall(), TokenCollection.newEmptyCollection());
        assertNotEquals(this.testGame.getGameBoard().getDashBoards().get("D").cloneEntranceHall(), TokenCollection.newEmptyCollection());
    }

    @Test
    void serializable(){
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }
}