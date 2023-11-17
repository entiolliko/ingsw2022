package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.visitor.base_commands.MoveToEntranceHall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveToEntranceHallTest {
    private Board testBoard;
    private String playerID;
    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    private MoveToEntranceHall testVisitor;
    Gson serializer;
    Gson deserializer;

    @BeforeEach
    void reset(){

        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        this.testBoard = testGame.getGameBoard();
        this.testVisitor = new MoveToEntranceHall("A", TokenEnum.GREEN);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
    }

    @Test
    void add_green() throws ReloadGameException {
        TokenCollection testTokenCollection = testGame.getGameBoard().getDashBoards().get("A").cloneEntranceHall();
        testGame.accept(testVisitor);
        testVisitor.getPlayerID();
        assertEquals(testTokenCollection.get(TokenEnum.GREEN) + 1, testBoard.getDashBoards().get("A").cloneEntranceHall().get(TokenEnum.GREEN));
    }



}
