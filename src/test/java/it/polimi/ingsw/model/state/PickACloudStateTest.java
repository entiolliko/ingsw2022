package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.visitor.player_visitor_command.PickCloud;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PickACloudStateTest {
    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    private State state;

    @BeforeEach
    void reset(){
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        state = new PickACloudState(testGame, playersID, 1);

    }

    @Test
    void testNextState(){
        PlayerVisitorCommand pickACloud = new PickCloud("A",1);
        //assertEquals(state, state.nextState(new PlayApprenticeCard("A", 2)));
    }

    @Test
    void testNextState4(){
        state = new PickACloudState(testGame, playersID, 3);
        PlayerVisitorCommand pickACloud = new PickCloud("A",1);
        //To test this I would need to implement the whole sequence of moves up to this point
    }

    @Test
    void testNextState1(){
        PlayerVisitorCommand pickACloud = new PickCloud("A",1);
        State temp = state.nextState(pickACloud);
        assertEquals(2, temp.currentPlayer);
        assertEquals("PlaceTokensState", temp.getClass().getSimpleName());
    }
}