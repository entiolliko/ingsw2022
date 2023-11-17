package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayApprenticeCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PlayApprenticeCardStateTest extends PlayApprenticeCardState {

    public PlayApprenticeCardStateTest() {
        super(new Game(
                List.of("a", "b", "c", "d"),
                List.of("a", "b", "c", "d"),
                List.of("a", "b", "a", "b"),
                "0",
                TypeOfGame.NORMAL
        ));

    }

    public static Stream<Arguments> defaultTestSuite() {
        return StateTest.defaultTestSuite();
    }

    @DisplayName("play ApprenticeCard Should Evolve Correctly")
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void playApprenticeCardShouldEvolveCorrectly(Game game) throws ReloadGameException {
        State tested = new PlayApprenticeCardState(game);
        List<String> players = game.getPlayersClockwise();
        int i;
        for (i = 0; i < game.getPlayersClockwise().size() - 1; i++) {
            tested = tested.run(new PlayApprenticeCard(players.get(i), i + 1));
            assertEquals(PlayApprenticeCardState.class, tested.getClass());
        }
        tested = tested.run(new PlayApprenticeCard(players.get(i), i + 1));
        assertEquals(PlaceTokensState.class, tested.getClass());
        assertNotEquals(PlayApprenticeCardState.class, tested.getClass());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void testAcceptedCommands(Game game) {
        State tested = new PlayApprenticeCardState(game);
        List<String> whatIExpect = List.of("PlayApprenticeCard");
        List<String> actualAllowedCommand =
                StateTest.getAllCommands().stream()
                                .filter(command -> tested.getAllowedCommands().contains(command)).toList();

        assertEquals(whatIExpect, actualAllowedCommand);
    }

}