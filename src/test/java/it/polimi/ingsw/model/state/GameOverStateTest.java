package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.visitor.player_visitor_command.MoveMotherNature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class GameOverStateTest {
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
        state = new GameOverState(testGame, playersID);
    }

    @Test
    void testing(){
        State temp = state.nextState(new MoveMotherNature("A", 2));
    }
}