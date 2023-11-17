package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayApprenticeCardTest {
    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    private PlayApprenticeCard testVisitor;
    Gson serializer;
    Gson deserializer;
    PlayerVisitorCommand testCommand;

    @BeforeEach
    void reset(){

        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.NORMAL);
        this.testVisitor = new PlayApprenticeCard("A", 1);
        this.testCommand = new PlayApprenticeCard("A", 1);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
    }

    @Test
    void correctConstructor(){
        testCommand.toString();
        assertEquals("A", this.testVisitor.playerID);
        assertEquals(1, this.testVisitor.cardID);
    }

    void printStackTrace(PlayApprenticeCard visitor, Game testGame) {
        try{
            testGame.accept(visitor);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void AlreadyPlayedCardException() throws ReloadGameException{
        testGame.accept(testVisitor);
        assertThrows(ReloadGameException.class, () -> testGame.accept(testVisitor));
        //printStackTrace(testVisitor, testGame);
    }

    @Test
    void OutOfBoundIntegerCardException(){
        assertThrows(ReloadGameException.class, () -> testGame.accept(new PlayApprenticeCard("A", 0)));
        //printStackTrace(new PlayApprenticeCard("A", 0), testGame);
    }

    @Test
    void IllegalApprenticeCard() throws ReloadGameException {
        testGame.accept(testVisitor);
        assertThrows(ReloadGameException.class,()->testGame.accept(new PlayApprenticeCard("B", 1)));
        assertThrows(ReloadGameException.class,()-> new PlayApprenticeCard("B", 1).visit(testGame));
        assertThrows(ReloadGameException.class,()-> new PlayApprenticeCard("B", 1).visit(testGame.getGameBoard()));
        assertThrows(ReloadGameException.class,()->testGame.getGameBoard().accept(new PlayApprenticeCard("B", 1)));
        printStackTrace(new PlayApprenticeCard("B", 1), testGame);
    }

    @Test
    void serializable(){
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }

}
