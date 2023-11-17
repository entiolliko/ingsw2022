package it.polimi.ingsw.controller.server.lobby;

import it.polimi.ingsw.client.backend.DefaultConnector;
import it.polimi.ingsw.controller.communication_protocol.client_requests.LeaveReq;
import it.polimi.ingsw.controller.server.welcome_server.ConcreteServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.lang.Thread.sleep;

class ConcreteLobbyTest extends Lobby{

    static ConcreteServer testserver;
    static List<DefaultConnector> testclients;
    static ExecutorService pool;

    private static int serverport;
    static{
        try {
            ServerSocket socket = new ServerSocket(0);
            serverport = socket.getLocalPort();
            socket.close();
            sleep(100);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    protected ConcreteLobbyTest() {
        super("666");
    }

    @BeforeEach
    void reset() throws InterruptedException {
        pool = Executors.newFixedThreadPool(5);
        pool.execute(testserver);
        sleep(10);
        testclients.forEach(client -> {
            try {
                sleep(100);
                client.connectToServer("localhost", serverport);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        sleep(100);
    }

    @BeforeAll
    static void startClients(){
        testclients = new ArrayList<>();
        testserver = new ConcreteServer(serverport);
        for (int i = 0; i < 4; i++) {
            testclients.add(new DefaultConnector());
        }
    }
    @AfterEach
    void after(){
        testclients.forEach(client->client.sendRequest(new LeaveReq()));
        pool.shutdownNow();
    }

    @Test
    void gameStart(){
        assertEquals(1, 1);
        //TODO
    }


}