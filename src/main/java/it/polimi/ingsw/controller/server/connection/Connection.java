package it.polimi.ingsw.controller.server.connection;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.controller.communication_protocol.server_responses.DisconnectedRes;
import it.polimi.ingsw.controller.server.Service;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.debug_utility.DebugLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * The connection class holds the connection socket with the client.
 * It listens for messages and is used for sending messages but does not respond directly to them, unless they are pings
 *
 * When instantiated three threads are activated:
 *      -the listenThread, which listens for client requests
 *      -the ping sender, which sends ping message to the client in order to confirm that the connection is alive
 *      -the ping timer, which severs the connection is no ping is received during its countdown
 */
public abstract class Connection implements Runnable, ServerAcceptor {
    private final Socket socket;
    protected final ServerPingTimer timer;
    private final  ServerPingSender pingSender;
    private final Scanner in;
    private final PrintWriter out;
    private final Gson serializer;
    private final Gson deserializer;
    private String name;
    private Service service;

    /**
     * creates a new connection
     * @param socket socket that contains the client connection
     * @param service service provider the connection is linked to
     * @throws IOException if the connection falls during the construction
     */
    protected Connection(Socket socket, Service service) throws IOException {
        this.socket = socket;

        this.service = service;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
        this.timer = new ServerPingTimer(this);
        this.pingSender = new ServerPingSender(this);
        timer.activate();
        pingSender.activate();
        new Thread(this).start();
    }

    /**
     * gets the player's nickname
     * @return player's nickname
     */
    public synchronized String getName() {
        return name;
    }

    /**
     * changes the player's nickname
     * @param name new player's nickname
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * listening routine, can be externally closed or does so automatically if the socket connection is closed
     */
    @Override
    public void run() {
        boolean go = true;
        while (go) {
            if (in.hasNextLine()) {
                String line = in.nextLine();
                ClientRequest request;
                try {
                    request = deserializer.fromJson(line, ClientRequest.class);

                    synchronized (this) {
                        this.accept(request);
                    }
                } catch (RuntimeException e) {
                    DebugLogger.log("WIPConnection, an invalid clientPackage was sent", Level.WARNING);
                    DebugLogger.log("content was" + line, Level.WARNING);
                    e.printStackTrace();
                }
            } else {
                //service.acceptCommand(this, new ClientPackage(ClientRequestType.LEAVE, null)); // asks to quit by itself
                go = false;
            }
        }

    }

    /**
     * sends a disconnection message to the client and then severs the connection
     */
    public synchronized void kick(String reason) {
        send(new DisconnectedRes(reason));
        leave();
    }

    /**
     * severs the connection with the client
     */
    public synchronized void disconnect() {
        try {
            timer.deactivate();
            pingSender.deactivate();
            socket.close();
        } catch (IOException e) {
            leave(); // try until you succeed
        }
        DebugLogger.log("WIPConnection : player disconnected", Level.INFO);
    }

    /**
     * sends a string containing the serialized given response
     * @param response response to send
     */
    public void send(ServerResponse response) {
        out.println(serializer.toJson(response));
    }

    /**
     * returns a reference to the service provider
     * @return a reference to the service provider
     */
    protected synchronized Service getService() {
        return service;
    }

    /**
     * changes the service provider with the given one
     * @param service new service provider
     */
    public synchronized void setService(Service service) {
        this.service = service;
    }
}
