package it.polimi.ingsw.controller.utility;

import it.polimi.ingsw.client.frontend.command_line.game_prompt.CLIEventHandler;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;
import it.polimi.ingsw.model.game_event.game_events.*;
import it.polimi.ingsw.model.islands.TowerEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventBufferTest {
    private EventBuffer tested;
    @BeforeEach
    public void setup() {
        tested = new EventBuffer();
    }
    @Test
    void testIsEmpty() {
        tested.acceptEvent(new BagToCloudEvent(1, TokenCollection.newEmptyCollection().getMap()));
        tested.acceptEvent(new CloudToEntranceHallEvent(3, "joe", TokenCollection.newEmptyCollection().getMap()));
        tested.acceptEvent(new CoinsChangedEvent("ciro", 12));
        tested.acceptEvent(new CommandFailedEvent("dunno"));
        tested.acceptEvent(new CurrentPlayerChangedGameEvent("joe"));
        tested.acceptEvent(new GameIsOverEvent(List.of("nobodies")));
        tested.acceptEvent(new GameStartedEvent());
        tested.acceptEvent(new MergedIslandsEvent(1, 2));
        tested.acceptEvent(new MoveMotherNatureEvent(1, 2));
        tested.acceptEvent(new NewStateGameEvent("hi"));
        tested.acceptEvent(new PlayedApprenticeCardEvent("joe", 1));
        tested.acceptEvent(new ProfessorChangedEvent(TokenEnum.RED, "joe"));
        tested.acceptEvent(new TokenToIslandEvent("joe", 1, TokenEnum.RED));
        tested.acceptEvent(new TokenToStudyHallEvent("joe", TokenEnum.RED));
        tested.acceptEvent(new TowersToDashBoardEvent(TowerEnum.WHITE, "joe", 2));
        tested.acceptEvent(new TowersToIslandEvent(TowerEnum.WHITE, "joe", 3));
        List<GameEvent> events = tested.flush();
        GameEventHandler eventHandler = new CLIEventHandler(new PrintWriter(System.out));
        assertDoesNotThrow(() -> events.forEach(eventHandler::acceptEvent));
        for (int i = 0; i < events.size() - 1; i++) {
            assertNotEquals(events.get(i), events.get(i + 1));
        }




    }

}