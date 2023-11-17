package it.polimi.ingsw.controller.files_storage;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.state.PlayApprenticeCardState;
import it.polimi.ingsw.model.state.State;
import it.polimi.ingsw.model.state.StateTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameSaverTest {
    @BeforeAll
    static void purge(){
        GameSaver.cleanse();
    }
    @ParameterizedTest
    @MethodSource("testSuite")
    void testSerialization(Game game) throws IOException, InterruptedException {
        State testedState = new PlayApprenticeCardState(game);
        assertDoesNotThrow(() -> GameSaver.saveGame(testedState));
        TimeUnit.MILLISECONDS.sleep(10);
        assertEquals(testedState, GameSaver.loadGame(game.getGameID()));
    }


    @Test
    void showMaxId() {
        assertDoesNotThrow(() -> System.out.println(GameSaver.getMaxId()));
    }

    public static Stream<Arguments> testSuite() {
        return StateTest.defaultTestSuite();
    }

}