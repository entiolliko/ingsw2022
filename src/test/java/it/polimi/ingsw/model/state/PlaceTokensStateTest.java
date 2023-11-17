package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.dashboard.DashBoard;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.visitor.player_visitor_command.MoveToStudyHall;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PlaceTokensStateTest extends PlaceTokensState {

    private PlaceTokensStateTest() {
        super(null, null, 0);
    }
    public static Stream<Arguments> defaultTestSuite() {
        return StateTest.defaultTestSuite();
    }

    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void threeTokensForTwoPlayersGame(Game game) throws ReloadGameException {
        if (game.getPlayersClockwise().size() == 2) {
            NTokensForThisKindOfGame(3, game);
        }
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void fourTokensForThreePlayersGame(Game game) throws ReloadGameException {
        if (game.getPlayersClockwise().size() == 3) {
            NTokensForThisKindOfGame(4, game);
        }
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void threeTokensForFourPlayerGame(Game game) throws ReloadGameException {
        NTokensForThisKindOfGame(3, game);
    }
    private void NTokensForThisKindOfGame(int numberOfTokensToPlace, Game game) throws ReloadGameException {
        //Setting up
        //can't place tokens if I don't have them
        //and since the given tokens are random at the start of the game
        //I have to give the tokens I know I'll use
        String testedPlayer = game.getPlayersClockwise().get(0);
        DashBoard testedPlayerDashBoard = game.getGameBoard().getDashBoards().get(testedPlayer);


        State tested = new PlaceTokensState(game, game.getPlayersClockwise(), 0);
        for (int i = 0; i < numberOfTokensToPlace - 1; i++) {
            testedPlayerDashBoard.moveToEntranceHall(TokenEnum.RED);
            tested = tested.run(new MoveToStudyHall(testedPlayer,TokenEnum.RED));
            assertEquals(PlaceTokensState.class, tested.getClass());
        }
        testedPlayerDashBoard.moveToEntranceHall(TokenEnum.RED);
        tested = tested.run(new MoveToStudyHall(testedPlayer,TokenEnum.RED));
        assertNotEquals(PlaceTokensState.class, tested.getClass());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void PlayCharCardShouldNotAlterTheInternalState() {
        //TODO: test once character cards are implemented
        Object tr = true;
        assertEquals(true, tr);
    }



}