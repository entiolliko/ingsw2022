package it.polimi.ingsw.controller.server.welcome_server;

import it.polimi.ingsw.client.backend.DefaultConnector;
import it.polimi.ingsw.client.frontend.ClientOutInterface;
import it.polimi.ingsw.client.frontend.PromptSelector;
import it.polimi.ingsw.controller.communication_protocol.client_requests.CreateLobbyReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.JoinLobbyReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.LeaveReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.PingReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.*;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.character_cards.PlayCharacterCardReq;
import it.polimi.ingsw.controller.communication_protocol.server_responses.DisconnectedRes;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.controller.files_storage.GameSaver;
import it.polimi.ingsw.controller.server.lobby.ILobby;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class ConcreteServerTest extends Server{
    ConcreteServer testserver;
    DefaultConnector testclient;
    ExecutorService serverThread;
    private static final int serverport = 0;

    protected ConcreteServerTest() {
        super(serverport);
    }

    @BeforeEach
    void reset() throws InterruptedException {
        this.testserver = new ConcreteServer(serverport);
        serverThread = Executors.newSingleThreadExecutor();
        serverThread.execute(testserver);
        sleep(100);
        this.testclient = new DefaultConnector();
    }
    @AfterEach
    void after(){
        testclient.disconnect();
        serverThread.shutdownNow();
    }

    void connect() throws IOException {
        int port = this.testserver.serverSocket.getLocalPort();
        this.testclient.connectToServer("localhost", port);
    }

    void connect(@NotNull DefaultConnector toConnect) throws IOException {
        int port = this.testserver.serverSocket.getLocalPort();
        toConnect.connectToServer("localhost", port);
    }

    void createLobby(String hostname, int numberOfPlayers, TypeOfGame type) throws IOException {
        this.connect();
        this.testclient.sendRequest(new CreateLobbyReq(hostname, numberOfPlayers, type));
    }


    @Test
    void testConnection() throws IOException, InterruptedException {
        this.connect();
        sleep(100);
        assertEquals(1, this.testserver.limbo.size());
    }

    @Test
    void testCreateLobby() throws IOException, InterruptedException {
        int expectedSize = Integer.parseInt(GameSaver.getMaxId()) + 1;
        this.createLobby("a", 2, TypeOfGame.NORMAL);
        sleep(10);
        assertEquals(1, this.testserver.lobbies.size());
        assertEquals(Set.of(String.valueOf(expectedSize)), this.testserver.lobbies.keySet());
    }

    @Test
    void startGame() throws IOException, InterruptedException {
        this.createLobby("a", 2, TypeOfGame.NORMAL);
        DefaultConnector b = new DefaultConnector();
        this.connect(b);
        int lobbyID = testserver.lobbies.keySet().stream().mapToInt(Integer::valueOf).max().orElse(0);
        b.sendRequest(new JoinLobbyReq("b", String.valueOf(lobbyID)));
        sleep(1000);
        assertEquals(1, this.testserver.lobbies.size());

    }
    @Test
    void load_game_n_play() throws IOException, InterruptedException {
        DefaultConnector a = new DefaultConnector();
        DefaultConnector b = new DefaultConnector();
        DefaultConnector c = new DefaultConnector();
        DefaultConnector d = new DefaultConnector();
        DefaultConnector e = new DefaultConnector();
        PromptSelector pa = new PromptSelector();
        PromptSelector pb = new PromptSelector();
        PromptSelector pc = new PromptSelector();
        PromptSelector pd = new PromptSelector();
        PromptSelector pe = new PromptSelector();
        a.addObserver(pa);
        a.addObserver(pb);
        a.addObserver(pc);
        a.addObserver(pd);
        e.addObserver(pe);
        this.connect(b);
        this.connect(a);
        this.connect(c);
        this.connect(d);
        this.connect(e);
        int lobbyID = 666;
        a.sendRequest(new JoinLobbyReq("sleepy hoe", String.valueOf(lobbyID)));
        b.sendRequest(new JoinLobbyReq("sleepy hoe", String.valueOf(lobbyID)));
        b.sendRequest(new JoinLobbyReq("sleepy joe", String.valueOf(lobbyID)));
        c.sendRequest(new JoinLobbyReq("mexican guy", String.valueOf(lobbyID)));
        d.sendRequest(new JoinLobbyReq("orange man", String.valueOf(lobbyID)));

        b.sendRequest(new PlayApprenticeCardReq(1));
        a.sendRequest(new PlayApprenticeCardReq(1));
        a.sendRequest(new PlayApprenticeCardReq(1));
        d.sendRequest(new PlayApprenticeCardReq(2));
        c.sendRequest(new PlayApprenticeCardReq(4));
        b.sendRequest(new PlayApprenticeCardReq(3));
        sleep(100);
        e.sendRequest(new JoinLobbyReq("johhny joe", String.valueOf(lobbyID)));
        e.sendRequest(new JoinLobbyReq("john", "impossible lobby"));

        System.out.println("a now playin");
        for (int i = 0; i < 4; i++) {
            a.sendRequest(new MoveToStudyHallReq(TokenEnum.GREEN));
            a.sendRequest(new MoveToStudyHallReq(TokenEnum.BLUE));
            a.sendRequest(new MoveToStudyHallReq(TokenEnum.VIOLET));
            a.sendRequest(new MoveToStudyHallReq(TokenEnum.RED));
            a.sendRequest(new MoveToStudyHallReq(TokenEnum.YELLOW));
            a.sendRequest(new PickCloudReq(0));
        }
        b.sendRequest(new LeaveReq());
        this.connect(b);
        b.sendRequest(new MoveMotherNatureReq(1));
        b.sendRequest(new MoveToIslandReq(TokenEnum.GREEN, 2));
        b.sendRequest(new PickCloudReq(0));
        b.sendRequest(new PingReq());
        b.sendRequest(new PlayCharacterCardReq("1", TokenEnum.GREEN));
        assertThrows(FailedLoginException.class, ()->{throw new FailedLoginException();});
        assertThrows(FailedServerSocketCreationException.class, ()->{throw new FailedServerSocketCreationException(9, new IOException());});
        assertThrows(FailedClientConnectionException.class, ()->{throw new FailedClientConnectionException(new IOException());});
        assertThrows(FailedServerConnectionException.class, ()->{throw new FailedServerConnectionException("boy ", new IOException());});
        assertThrows(LobbyFullException.class, () -> {throw  new LobbyFullException();});
        assertThrows(NonExistentGameException.class, () -> {throw  new NonExistentGameException();});

        sleep(1000);
    }
    @Test
    void joinFullLobby() throws IOException {
        DefaultConnector a = new DefaultConnector();
        a.addObserver(new PromptSelector());
        this.connect(a);
        a.sendRequest(new CreateLobbyReq("johnny", 7, TypeOfGame.EXPERT));
        assertThrows(InvalidArgsException.class, () -> {throw new InvalidArgsException("bro");});
    }
}