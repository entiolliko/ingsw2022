package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrepareForNextRoundTest {

    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    private Board testBoard;

    @BeforeEach
    void reset(){

        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        this.testBoard = testGame.getGameBoard();
    }

    @Test
    void correctlyResets() throws ReloadGameException {
        Game unchanged = testGame;
        for(int i = 0; i < testGame.getGameBoard().getClouds().size(); i++)
            testGame.getGameBoard().getClouds().get(i).removeTokens();
        testGame.accept(new PrepareForNextRound());
        assertEquals(unchanged, testGame);
    }

}
