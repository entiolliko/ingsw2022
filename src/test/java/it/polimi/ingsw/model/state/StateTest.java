package it.polimi.ingsw.model.state;

import com.google.gson.Gson;
import it.polimi.ingsw.model.visitor.player_visitor_command.*;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.visitor.base_commands.MoveToEntranceHall;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {
    private final static Gson serializer = CustomJsonBuilder.createSerializer();
    private final static Gson deserializer = CustomJsonBuilder.createDeserializer();

    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void constructorShouldNeverFail(Game game) {
        assertDoesNotThrow(() -> new PlayApprenticeCardState(game));
        for (int i = 0; i < game.getPlayersClockwise().size(); i++) {
            int a = i;
            assertDoesNotThrow(() -> new PlaceTokensState(game, game.getPlayersClockwise(), a));
            assertDoesNotThrow(() -> new MoveMotherNatureState(game, game.getPlayersClockwise(), a)); // usually
            assertDoesNotThrow(() -> new PickACloudState(game, game.getPlayersClockwise(), a));
        }
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void shouldNotAcceptCommandOutOfTurn(Game game) throws ReloadGameException {
        List<String> players = game.getPlayersClockwise();
        State tested = new PlayApprenticeCardState(game);
        State expected = new PlayApprenticeCardState(game);
        expected.getCurrentPlayer();
        for (int i = 1; i < players.size(); i++) {

            State finalTested = tested;
            int finalI = i;
            assertThrows(ReloadGameException.class, () -> finalTested.run(new PlayApprenticeCard(players.get(finalI), 1)));
        }
        tested = tested.run(new PlayApprenticeCard(players.get(0), 1));
        assertNotEquals(expected, tested);
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void illegalCommandsShouldNotBeAllowed(Game game) {
        String currentPlayer = game.getPlayersClockwise().get(0);
        State tested = new PlayApprenticeCardState(game);

        assertThrows(ReloadGameException.class, () -> tested.run(new PlayerVisitorCommand() {
            @Override
            public String getPlayerID() {
                System.out.println(this.getClass());
                return currentPlayer;
            }

            @Override
            public String getClassName() {
                return this.getClass().getName();
            }

        }));

    }

    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void testHashCode(Game game) {
        List <String> players = game.getPlayersClockwise();
        int expected = new MoveMotherNatureState(game, players, 1).hashCode();
        int actual = new MoveMotherNatureState(game, players, 1).hashCode();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void serializeTest(Game tested) {
        State testedState = new PlayApprenticeCardState(tested);
        assertEqualsSerialized(testedState);
    }

    public static void assertEqualsSerialized(State tested) {
        String json = serializer.toJson(tested);
        State actual = deserializer.fromJson(json, State.class);
        assertEquals(tested, actual);
    }

    public static Stream<Arguments> defaultTestSuite() {
        return Stream.of(
                Arguments.of(
                        //new Game(List.of(),List.of(), List.of(), String.valueOf(new Random().nextInt(1000)))
                        new Game(List.of("sleepy joe", "sleepy hoe", "orange man", "mexican guy"), List.of("gandalf", "dumbledore", "oz", "forrest"), List.of("millennial leftists", "millennial leftists", "confederates",  "confederates"), "1000", TypeOfGame.EXPERT)
                ),
                Arguments.of(
                        new Game(List.of("sleepy joe", "sleepy hoe", "orange man", "mexican guy"), List.of("gandalf", "dumbledore", "oz", "forrest"), List.of("millennial leftists", "millennial leftists", "confederates",  "confederates"), "750", TypeOfGame.EXPERT)
                ),
                Arguments.of(
                        new Game(List.of("sleepy joe", "sleepy hoe", "orange man", "mexican guy"), List.of("gandalf", "dumbledore", "oz", "forrest"), List.of("millennial leftists", "millennial leftists", "confederates",  "confederates"), "666", TypeOfGame.EXPERT)
                )
        );
    }
    public static List<String> getAllCommands() {
        return List.of(
                MoveMotherNature.class.getSimpleName(),
                MoveToEntranceHall.class.getSimpleName(),
                MoveToIsland.class.getSimpleName(),
                MoveToStudyHall.class.getSimpleName(),
                PickCloud.class.getSimpleName(),
                PlayApprenticeCard.class.getSimpleName()
                //TODO : add char cards command(s)
        );
    }


}