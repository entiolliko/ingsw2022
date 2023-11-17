package it.polimi.ingsw.controller.files_storage;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.game_event.GameEventReceiver;
import it.polimi.ingsw.model.state.State;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameSaver {
    private static final String SEPARATOR;
    private static final String PATH;
    private static final String EXTENSION = ".json";
    private static final List<String> NUMBER_CHARS;

    private static GameSaver instance;

    static {
        SEPARATOR = FileSystems.getDefault().getSeparator();
        PATH = System.getProperty("user.dir") + SEPARATOR + "saves";
        File folder = new File(PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }

        List<String> allowedChars = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            allowedChars.add(String.valueOf(i));
        }
        NUMBER_CHARS = allowedChars.stream().toList();

    }

    private final Gson serializer = CustomJsonBuilder.createSerializer();
    private final Gson deserializer = CustomJsonBuilder.createDeserializer();

    private static GameSaver getInstance() {
        if (instance == null)
            instance = new GameSaver();
        return instance;
    }

    public static void saveGame(State gameState) {
        new Thread(() -> saveGameThreaded(gameState)).start();
    }

    private static synchronized void saveGameThreaded(State gameState) {
        synchronized (gameState) {
            DebugLogger.log("saving gameState", Level.INFO);
            String savedGameName = gameState.getGame().getGameID() + EXTENSION;
            File gameFile = new File(PATH + SEPARATOR + savedGameName);

            try (FileWriter writer = new FileWriter(gameFile)) {
                Set<GameEventReceiver> receivers = gameState.getGame().popReceivers();
                writer.write(getInstance().serializer.toJson(gameState));
                receivers.forEach(gameState.getGame()::addEventObserver);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DebugLogger.log("gameState was saved", Level.INFO);
        }
    }

    public static synchronized State loadGame(String gameId) throws IOException {
        String savedGameName = PATH + SEPARATOR + gameId + EXTENSION;
        DebugLogger.log("loading game " + savedGameName, Level.INFO);
        Reader reader = Files.newBufferedReader(Paths.get(savedGameName));
        return getInstance().deserializer.fromJson(reader, State.class);
    }

    public static String getMaxId() {
        String[] filesNames = new File(PATH).list();
        if (Objects.isNull(filesNames)) return "0";
        return String.valueOf(Arrays
                .stream(filesNames)
                .map(filename -> filename.split("\\.")[0])
                .filter(GameSaver::isANumber)
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0))
                ;
    }

    public static Set<String> loadableLobbies(Set<String> activeLobbies) {

        String[] fileNames = Optional.ofNullable(new File(PATH).list()).orElse(new String[0]);
        return Stream.of(fileNames)
                .map(name -> name.split("\\.")[0])
                .filter(name -> !activeLobbies.contains(name))
                .collect(Collectors.toSet());
    }

    public static void cleanse() {
        Arrays
                .stream(
                        Objects
                                .requireNonNull(new File(PATH).listFiles()))
                .forEach(File::delete)
        ;
    }

    private static boolean isANumber(String str) {

        for (char character : str.toCharArray()) {
            if (!NUMBER_CHARS.contains(String.valueOf(character))) return false;
        }
        return true;
    }
}
