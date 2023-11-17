package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FillCloudsTest {
    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    FillClouds testVisitor;

    @BeforeEach
    void reset(){
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
        this.testVisitor = new FillClouds();
    }

    @Test
    void testCommand(){
        for(int i = 0; i < testGame.getGameBoard().getClouds().size(); i++)
            testGame.getGameBoard().getClouds().get(i).removeTokens();
        try{this.testGame.accept(testVisitor);}
        catch (Exception e){}
        Board br = testGame.getGameBoard();
        assertEquals(4, br.getClouds().size());
        assertNotEquals(br.getClouds().get(0).removeTokens(), TokenCollection.newEmptyCollection());
        assertNotEquals(br.getClouds().get(1).removeTokens(), TokenCollection.newEmptyCollection());
        assertNotEquals(br.getClouds().get(2).removeTokens(), TokenCollection.newEmptyCollection());
        assertNotEquals(br.getClouds().get(3).removeTokens(), TokenCollection.newEmptyCollection());
    }

    @Test
    void testErrors(){
        Board br = testGame.getGameBoard();
        br.getBag().randomPop(br.getBag().size() - 1);
        for(int i = 0; i < testGame.getGameBoard().getClouds().size(); i++)
            testGame.getGameBoard().getClouds().get(i).removeTokens();
        try{this.testGame.accept(testVisitor);}
        catch (Exception e){}


        assertEquals(1, br.getClouds().get(0).removeTokens().size());
        assertEquals(br.getClouds().get(1).removeTokens(), TokenCollection.newEmptyCollection());
        assertEquals(br.getClouds().get(2).removeTokens(), TokenCollection.newEmptyCollection());
        assertEquals(br.getClouds().get(3).removeTokens(), TokenCollection.newEmptyCollection());
    }

}