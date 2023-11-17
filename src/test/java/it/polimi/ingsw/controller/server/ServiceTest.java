package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.exceptions.LobbyFullException;
import it.polimi.ingsw.controller.exceptions.NameAlreadyUsedException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ServiceTest {
    Service test = new Service() {
    };

    @Test
    void serviceTest() throws ServerException {
        assertThrows(ServerException.class, ()->test.createLobby(null, null, 1, null));
        assertThrows(ServerException.class, ()->test.joinLobby(null, null, null));
        test.leave(null);
        assertThrows(ServerException.class, ()->test.moveToIsland(null, null, 1));
        assertThrows(ServerException.class, ()->test.moveToStudyHall(null, null));
        assertThrows(ServerException.class, ()->test.pickCloud(null, 0));
        assertThrows(ServerException.class, ()->test.playApprenticeCard(null, 1));
        assertThrows(ServerException.class, ()->test.playCharacterCard(null, null, null, 1, null, null));
        assertThrows(ServerException.class, ()->test.moveMotherNature(null, 1));
        assertThrows(ServerException.class, ()->test.defaultError());
        assertThrows(NameAlreadyUsedException.class, ()->{throw new NameAlreadyUsedException("ue");});
        assertThrows(LobbyFullException.class, ()->{throw new LobbyFullException("ue");});
    }
}