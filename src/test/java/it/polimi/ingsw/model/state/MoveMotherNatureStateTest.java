package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.IllegalApprenticeCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import it.polimi.ingsw.model.visitor.player_visitor_command.MoveMotherNature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveMotherNatureStateTest extends MoveMotherNatureState {

    private MoveMotherNatureStateTest() {
        super(null, null, 0);
    }
    public static Stream<Arguments> defaultTestSuite() {
        return StateTest.defaultTestSuite();
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void MoveMotherNatureShouldMoveCorrectly(Game game) throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException, ReloadGameException {
        String currentPlayer = game.getPlayersClockwise().get(0);
        State tested = new MoveMotherNatureState(game, game.getPlayersClockwise(), 0); //they are set as clockwise just for simplicity's sake
        game.getGameBoard().getCardHandler().playCard(currentPlayer, 8);
        tested = tested.run(new MoveMotherNature(currentPlayer, 5));
        assertEquals(4, game.getGameBoard().getIslands().getCurrMotherNaturePos());
    }
}
