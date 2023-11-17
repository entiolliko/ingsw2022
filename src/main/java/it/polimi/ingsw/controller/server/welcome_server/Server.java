package it.polimi.ingsw.controller.server.welcome_server;

import it.polimi.ingsw.controller.communication_protocol.server_responses.LobbiesStatusRes;
import it.polimi.ingsw.controller.communication_protocol.server_responses.ServerWelcomeRes;
import it.polimi.ingsw.controller.exceptions.InvalidArgsException;
import it.polimi.ingsw.controller.exceptions.NonExistentGameException;
import it.polimi.ingsw.controller.exceptions.ServerException;
import it.polimi.ingsw.controller.files_storage.GameSaver;
import it.polimi.ingsw.controller.utility.ProgressiveNumberGenerator;
import it.polimi.ingsw.controller.server.Service;
import it.polimi.ingsw.controller.server.connection.ConcreteConnection;
import it.polimi.ingsw.controller.server.connection.Connection;
import it.polimi.ingsw.controller.server.lobby.ConcreteLobby;
import it.polimi.ingsw.controller.server.lobby.ILobby;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * The server consists of:
 *      - a serverSocket to establish connections with the clients
 *      - a hub -called limbo- where all clients who aren't currently engaged are
 *      - a collection of the currently loaded lobbies
 *
 */
public abstract class Server implements Runnable, Service {

    private final int port;

    protected final Set<Connection> limbo;
    protected final Map<String, ILobby> lobbies;
    protected final ProgressiveNumberGenerator numberGenerator;
    protected ServerSocket serverSocket;

    private ExecutorService listenThread;

    private final Object goLock = new Object();
    private boolean go;

    /**
     * creates a server instance
     * @param port port on which the server operates
     */
    protected Server(int port) {
        this.port = port;

        this.limbo = new HashSet<>();
        this.lobbies = new HashMap<>();

        this.numberGenerator = new ProgressiveNumberGenerator(Integer.parseInt(GameSaver.getMaxId()) + 1);

        go = true;
    }

    public static void main(String[] args) {
        DebugLogger.setLevel(Level.INFO);
        if (Arrays.stream(args).toList().contains("-purge")) {
            GameSaver.cleanse();
            DebugLogger.log("all saves have been deleted", Level.SEVERE);
        }

        try {
            new ConcreteServer(Integer.parseInt(args[0])).run();
        } catch (RuntimeException e) {
            new ConcreteServer(9001).run();
        }
    }

    /**
     * shuts the server down
     */
    public void shutDown() {
        synchronized (goLock) {
            go = false;
        }
        if (!Objects.isNull(listenThread))
            listenThread.shutdown();
    }
    /**
     * starts the server
     */
    @Override
    public void run() {
        synchronized (goLock) {
            go = true;
        }
        listenThread = Executors.newSingleThreadExecutor();
        listenThread.submit(this::listen);
    }
    private void listen() {
        createServerSocket();
        DebugLogger.log("server is at " + serverSocket.getLocalSocketAddress().toString(), Level.SEVERE);
        boolean keepAlive = true;
        while (keepAlive) {
            try {
                Socket newSocket = serverSocket.accept();
                synchronized (limbo) {
                    Connection newConnection = new ConcreteConnection(newSocket, this);
                    this.limbo.add(newConnection);
                    newConnection.send(new ServerWelcomeRes(lobbies.keySet(), GameSaver.loadableLobbies(lobbies.keySet())));
                    DebugLogger.log("connected", Level.INFO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (goLock) {
                keepAlive = go;
            }
        }
        DebugLogger.log("server shutting down", Level.SEVERE);
    }

    private void createServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes a socket from the limbo
     * @param sender connection to sever
     */
    public void disconnect(Connection sender) {
        synchronized (limbo) {
            limbo.remove(sender);
        }
    }

    /**
     * creates a new lobby in a no game state and adds it to its collection
     * @param numberOfPlayers desired number of players
     * @param gameMode desired game mode
     * @return id of the created lobby
     * @throws InvalidArgsException throw if the given args are invalid: i.e., the number of desired players is none of the following: 2, 3 or 4
     */
    public String addNewLobby(int numberOfPlayers, TypeOfGame gameMode) throws InvalidArgsException {
        String gameID = String.valueOf(numberGenerator.nextValue());
        synchronized (lobbies) {
            lobbies.put(gameID, new ConcreteLobby(gameID, numberOfPlayers, gameMode));
        }
        notifyLimbo();
        return gameID;
    }

    private void notifyLimbo() {
        limbo.forEach(connection -> connection.send(new LobbiesStatusRes(lobbies.keySet(), GameSaver.loadableLobbies(lobbies.keySet()))));
    }

    private void tryLoadingLobby(String gameId) throws NonExistentGameException {
        try {
            lobbies.put(gameId, new ConcreteLobby(gameId));
        } catch (RuntimeException e) {
            DebugLogger.log("Requested lobby isn't on disk", Level.WARNING);
            e.printStackTrace();
            throw new NonExistentGameException();
        }
    }

    /**
     * tries to add player to lobby
     * @param gameId id of the lobby the player wants to join
     * @param sender player who wants to join
     * @throws ServerException thrown when:
     *      - the lobby is in a game state
     *      - player tries to log with a name that is currently in use
     */
    public void addToLobby(String gameId, Connection sender) throws ServerException {
        synchronized (limbo) {
            synchronized (lobbies) {
                if (!lobbies.containsKey(gameId))
                    tryLoadingLobby(gameId);

                moveRequestSenderToLobby(gameId, sender);
            }
        }
    }

    private void moveRequestSenderToLobby(String gameId, Connection sender) throws ServerException {
        lobbies.get(gameId).onAddPlayer(sender);
        limbo.remove(sender);
        sender.setService(lobbies.get(gameId)); // setService is already synchronized
    }

}
