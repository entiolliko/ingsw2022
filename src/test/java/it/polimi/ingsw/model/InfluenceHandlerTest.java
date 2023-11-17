package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.custom_data_structures.Team;
import it.polimi.ingsw.model.dashboard.DashBoard;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.IslandChain;
import it.polimi.ingsw.model.state.StateTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InfluenceHandlerTest {
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void testBasicFunctionality(Game game) throws ReloadGameException {
        String player = game.getPlayersClockwise().get(0);
        Board curr = game.getGameBoard();
        DashBoard currPlayerDB =curr.getDashBoards().get(player);

        addTokensToStudyHall(curr, currPlayerDB, 3);

        //adds a red token to the island
        addToIsland(curr, TokenEnum.RED, 0);
        assertEquals(curr.getIslands().getTeamOf(player).get().getName(), curr.getInfluenceHandler().winner(curr.getIslands(), curr.getProfessors()));
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void zeroPointsGiveNoneAsWinner(Game game) {
        Board curr = game.getGameBoard();
        IslandChain islands = curr.getIslands();
        Professors professors = curr.getProfessors();
        InfluenceHandler IH = curr.getInfluenceHandler();

        assertEquals(null, IH.winner(islands, professors));
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void bonusValueShouldWork(Game game) {
        Board curr =  game.getGameBoard();
        String currPlayer = game.getPlayersClockwise().get(0);
        Optional<String> playerTeam = curr.getIslands().getTeamOf(currPlayer).map(Team::getName);
        assert playerTeam.isPresent();

        curr.getInfluenceHandler().addBonus(currPlayer, 2);
        int actualScoreForPlayer = curr.getInfluenceHandler().getScores(curr.getIslands(), curr.getProfessors()).get(playerTeam.get()).size();
        assertEquals(2, actualScoreForPlayer);
    }



    private void addTokensToStudyHall(Board curr, DashBoard currPlayerDB, int amount) throws ReloadGameException {
        //give player red tokens and update status of professors
        for (int i = 0; i < amount; i++) {
            currPlayerDB.moveToStudyHall(TokenEnum.RED);
            curr.getProfessors().updateProfessors(curr.getDashBoards());
        }
    }
    private void addToIsland(Board curr, TokenEnum colour, int islandIndex) {
        curr.getIslands().addToken(colour, islandIndex);
    }


    public static Stream<Arguments> defaultTestSuite() {
        return StateTest.defaultTestSuite();
    }

}