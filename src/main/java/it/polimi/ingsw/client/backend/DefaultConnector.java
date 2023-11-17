package it.polimi.ingsw.client.backend;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.controller.communication_protocol.client_requests.LeaveReq;
import it.polimi.ingsw.controller.communication_protocol.server_responses.DisconnectedRes;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.debug_utility.DebugLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DefaultConnector extends Connector {
    private final Gson serializer;
    private final Gson deserializer;
    private Socket socket;
    private final ClientPingSender clientPingSender;
    private final ClientPingTimer clientPingTimer;
    private Scanner in;
    private PrintWriter out;
    private ExecutorService listenThread;

    public DefaultConnector() {
        super();
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
        this.clientPingSender = new ClientPingSender(this);
        this.clientPingTimer = new ClientPingTimer(this);
        listenThread = Executors.newSingleThreadExecutor();
    }

    @Override
    public synchronized void connectToServer(String ip, int port) throws IOException {
        disconnect();
        socket = new Socket(ip, port);
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.listen();
        this.clientPingSender.activate();
        this.clientPingTimer.activate();
        DebugLogger.log("connectToServer, procedure Over", Level.INFO);
    }
    public synchronized void disconnect() {
        closePreviousConnection();
        shutdownRoutine();
    }
    public void disconnect(ServerResponse response) {
        disconnect();
        updateAll(response);
    }
    public synchronized void quit() {
        if (!Objects.isNull(out)) {
            sendRequest(new LeaveReq());
        }
        disconnect();
    }


    private void closePreviousConnection() {
        if (!Objects.isNull(socket)) sendRequest(new LeaveReq());
    }
    private void shutdownRoutine() {
        resetListenerThread();
        clientPingSender.deactivate();
        clientPingTimer.deactivate();
    }

    private void resetListenerThread() {
        try {
            listenThread.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listenThread = Executors.newSingleThreadExecutor();
    }

    private void listen() {
        listenThread.execute(this::listenThreaded);

    }

    private void listenThreaded() {
        while (in.hasNextLine()) {
            try {
                clientPingTimer.refresh();
                ServerResponse received = receivedMessage();
                if (wasDisconnectedRequest(received)) {
                    disconnect(received);
                    return;
                }
                updateAll(received);
            } catch (RuntimeException e) {
                DebugLogger.log("Couldn't deserialize message", Level.WARNING);
            }
        }
        shutdownRoutine();
        updateAll(new DisconnectedRes("procedure over"));
    }

    private boolean wasDisconnectedRequest(ServerResponse received) {
        if (received.isDisconnectReq()) {
            DebugLogger.log("disconnection message was sent", Level.INFO);
            DebugLogger.log(socket.getPort() + " was the port", Level.INFO);
            return true;
        }
        return false;
    }

    private ServerResponse receivedMessage() {
        String message = in.nextLine();

        return this.deserializer.fromJson(message, ServerResponse.class);
    }

    @Override
    public synchronized void sendRequest(ClientRequest request) {
        String json = serializer.toJson(request);
        out.println(json);
    }

}
