package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.state.PlayApprenticeCardState;
import it.polimi.ingsw.model.state.State;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TwoPlayerNORMALTest {
    List<String> playersID;
    List<String> magicians;
    List<String> teams;
    String gameID;
    Game game;
    State gameState;

    @BeforeEach
    void ResetTest(){
        playersID = List.of("A","B");
        magicians = List.of("mA","mB");
        teams = List.of("A","n");
        gameID = "123.A)£$41";
        playersID = shuffleList(playersID);
        game = new Game(playersID, magicians, teams, gameID, TypeOfGame.NORMAL);
        gameState = new PlayApprenticeCardState(game);
    }

    @Test
    void checkRules() {
        assertEquals(playersID, gameState.getGame().getGameBoard().getPlayersID());

        assertEquals(100, gameState.getGame().getGameBoard().getBag().size());

        TokenCollection temp = TokenCollection.newEmptyCollection();
        for (int i = 0; i < gameState.getGame().getGameBoard().getIslands().numberOfIslands(); i++) {
            if (gameState.getGame().getGameBoard().getIslands().getCurrMotherNaturePos() != i &&
                    (gameState.getGame().getGameBoard().getIslands().getCurrMotherNaturePos() +
                            gameState.getGame().getGameBoard().getIslands().numberOfIslands() / 2) % gameState.getGame().getGameBoard().getIslands().numberOfIslands() != i) {
                assertEquals(1, gameState.getGame().getGameBoard().getIslands().getIslandTokens(i).size());
                temp.addToCollection(gameState.getGame().getGameBoard().getIslands().getIslandTokens(i));
            }
        }
        assertEquals(10, temp.size());
        for (int i = 0; i < TokenEnum.values().length; i++) {
            assertEquals(2, temp.get(Arrays.stream(TokenEnum.values()).toList().get(i)));
        }

        for (int i = 0; i < playersID.size(); i++){
            assertEquals(3, gameState.getGame().getGameBoard().getClouds().get(i).size());
            assertEquals(7, gameState.getGame().getGameBoard().getDashBoards().get(playersID.get(i)).cloneEntranceHall().size());
        }

        /*assertThrows(InvalidCommandException.class, ()-> gameState.run(new MoveMotherNature("A", 2)));
        assertThrows(InvalidCommandException.class, ()-> gameState.run(new MoveToEntranceHall("A", TokenEnum.GREEN)));
        assertThrows(InvalidCommandException.class, ()-> gameState.run(new MoveToIsland(TokenEnum.GREEN, 2, "A")));
        assertThrows(InvalidCommandException.class, ()-> gameState.run(new MoveToStudyHallFromTheEntrance("A", TokenEnum.GREEN)));
        assertThrows(InvalidCommandException.class, ()-> gameState.run(new PickCloud("A", 2)));
        */


    }

    private List<String> shuffleList(List<String> list){
        ArrayList<String> temp = new ArrayList<>(list);
        Collections.shuffle(temp);
        return new ArrayList<>(temp);
    }
}
