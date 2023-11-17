package it.polimi.ingsw.controller.server.connection;

import it.polimi.ingsw.controller.server.Service;
import it.polimi.ingsw.controller.server.welcome_server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteConnectionTest {
    Service service = new Service(){};


    ConcreteConnectionTest() throws IOException {
    }

    @Test
    void coverage() throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(0);
        Thread t = new Thread(()-> {
            try {
                socket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Socket sockets = new Socket();
        sockets.connect(socket.getLocalSocketAddress());

        ConcreteConnection test = new ConcreteConnection(sockets, service);
        test.createLobby(null, 1, null);
        test.joinLobby(null, null);
        test.moveToIsland(null, 1);
        test.moveMotherNature(0);
        test.leave();
        test.pickCloud(0);
        test.playApprenticeCard(0);
        test.playCharacterCard(null, null, 0, null, null);
        test.ping();
        test.moveToStudyHall(null);
        t.join();
    }
}