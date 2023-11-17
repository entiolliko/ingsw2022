package it.polimi.ingsw.controller.communication_protocol;

import it.polimi.ingsw.client.frontend.PromptSelector;
import it.polimi.ingsw.client.frontend.command_line.CLIScreen;
import it.polimi.ingsw.controller.communication_protocol.server_responses.*;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientAcceptorTest {
    private static final PromptSelector acceptor = new PromptSelector();
    @BeforeAll
    public static void ciro() {
        acceptor.setScreen(new CLIScreen());
    }
    @Test
    void testAcceptor() {
        acceptor.accept(new DisconnectedRes());
        acceptor.accept(new DisconnectedRes("bro"));
        acceptor.accept(new ErrorRes("broo"));
        assertThrows(RuntimeException.class, () -> acceptor.accept(new GameStatusRes(new GameDTO(List.of()), List.of())));

        acceptor.accept(new LobbiesStatusRes(Set.of("you"), Set.of("liar!")));
        acceptor.accept(new LobbyJoinedRes("oi", Set.of("ye", "old", "cunt")));
        acceptor.accept(new LobbyStandbyRes("oi", Map.of()));
        acceptor.accept(new ServerPing());
        assertDoesNotThrow(() -> acceptor.accept(new ServerWelcomeRes(Set.of("you"), Set.of("ain't"))));
    }
}