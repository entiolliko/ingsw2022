package it.polimi.ingsw.client.frontend.command_line;

import it.polimi.ingsw.client.frontend.ClientOutInterface;
import it.polimi.ingsw.client.frontend.command_line.game_prompt.CLIEventHandler;
import it.polimi.ingsw.client.frontend.command_line.game_prompt.GamePrompt;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CLIScreen implements ClientOutInterface {
    private final PrintWriter out;
    private final GamePrompt gamePrompt;
    private final GameEventHandler eventHandler;
    private String name;
    private final String title = """
                                                                                                                                          ,-     _.---     `._
                                                                                                                                        ,'     ,'-              `""\"'.
               ,ggggggg,                                                                                                             ,'    .-""`.    ,-'            `.
             ,dP""\"""\"Y8b                                              I8                                                           ,'    (        ,'                :
             d8'    a  Y8                                              I8                                                         ,'     ,'           __,            `.
             88     "Y8P'             gg                            88888888                                                ,""\""'     .' ;-.    ,  ,'  \\             `""\"".
             `8baaaa                  ""                               I8                                                 ,'           `-(   `._(_,'     )_                `.
            ,d8P""\""       ,gggggg,   gg     ,gggg,gg   ,ggg,,ggg,     I8    gg     gg    ,g,                            ,'         ,---. \\ @ ;   \\ @ _,'                   `.
            d8"            dP""\""8I   88    dP"  "Y8I  ,8" "8P" "8,    I8    I8     8I   ,8'8,                      ,-""'         ,'      ,--'-    `;'                       `.
            Y8,           ,8'    8I   88   i8'    ,8I  I8   8I   8I   ,I8,   I8,   ,8I  ,8'  Yb                    ,'            ,'      (      `. ,'                          `.
            `Yba,,_____, ,dP     Y8,_,88,_,d8,   ,d8b,,dP   8I   Yb, ,d88b, ,d8b, ,d8I ,8'_   8)                   ;            ,'        \\    _,','                            `.
              `"Y8888888 8P      `Y88P""Y8P"Y8888P"`Y88P'   8I   `Y888P""Y88P""Y88P"888P' "YY8P8P                 ,'            ;          `--'  ,'                              `.
                                                                                  ,d8I'                          ,'             ;          __    (                    ,           `.
                                                                                ,dP'8I                           ;              `____...  `78b   `.                  ,'           ,'
                                                                               ,8"  8I                           ;    ...----'''' )  _.-  .d8P    `.                ,'    ,'    ,'
                                                                               I8   8I              _....----''' '.        _..--"_.-:.-' .'        `.             ,''.   ,' `--'
                                                                               `8, ,8I                            `" ___ "" _.-'' .-'`-.:..___...--' `-._      ,-"'   `-'
                                                                                `Y8P"                       _.--'       _.-'    .'   .' .'               `""\"""
                                                                                                      __.-''        _.-'     .-'   .'  /
                                                                                                      '          _.-' .-'  .-'        .'
                                                                                                             _.-'  .-'  .-' .'  .'   /
                                                                                                        _.-'      .-'   .-'  .'   .' 
                                                                                                    _.-'       .-'    .'   .'    /
                                                                                                           _.-'    .-'   .'    .'
                                                                                                      .-'            .'
            """;

    public CLIScreen() {
        this.out = new PrintWriter(System.out, true);
        this.gamePrompt = new GamePrompt(out);
        this.eventHandler = new CLIEventHandler(out);
        promptMenu();
    }

    @Override
    public void promptMenu() {
        out.println(title);
        out.println("welcome! please select an option!");
        out.println("type : help for info!");
    }

    @Override
    public void promptServerConnection(Set<String> availableLobbies, Set<String> loadableLobbies) {
        out.println("you are now connected to the server!");
        out.println("""
                   _______________                        |*\\_/*|________
                  |  ___________  |     .-.     .-.      ||_/-\\_|______  |
                  | |           | |    .****. .****.     | |           | |
                  | |   0   0   | |    .*****.*****.     | |   0   0   | |
                  | |     -     | |     .*********.      | |     -     | |
                  | |   \\___/   | |      .*******.       | |   \\___/   | |
                  | |___     ___| |       .*****.        | |___________| |
                  |_____|\\_/|_____|        .***.         |_______________|
                    _|__|/ \\|_|_.............*.............._|________|_
                   / ********** \\                          / ********** \\
                 /  ************  \\                      /  ************  \\
                --------------------                    --------------------
                """);
        promptLobbies(availableLobbies, loadableLobbies);
    }

    @Override
    public void promptLobby(String gameID, Set<String> players) {

        out.println("welcome to the lobby#" + gameID + ", " + name);
        out.println("currently connected players are:");
        out.println(players);
    }

    @Override
    public void promptLobbyStandby(String gameId, Map<String, ConnectionStatusEnum> playersConnections) {
        out.println("""
                              ...
                             ;::::;
                           ;::::; :;
                         ;:::::'   :;
                        ;:::::;     ;.
                       ,:::::'       ;           OOO\\
                       ::::::;       ;          OOOOO\\      
                       ;:::::;       ;         OOOOOOOO      
                      ,;::::::;     ;'         / OOOOOOO     
                    ;:::::::::`. ,,,;.        /  / DOOOOOO   
                  .';:::::::::::::::::;,     /  /     DOOOO   
                 ,::::::;::::::;;;;::::;,   /  /        DOOO  
                ;`::::::`'::::::;;;::::: ,#/  /          DOOO 
                :`:::::::`;::::::;;::: ;::#  /            DOOO
                ::`:::::::`;:::::::: ;::::# /              DOO
                `:`:::::::`;:::::: ;::::::#/               DOO
                 :::`:::::::`;; ;:::::::::##                OO
                 ::::`:::::::`;::::::::;:::#                OO
                 `:::::`::::::::::::;'`:;::#                O 
                  `:::::`::::::::;' /  / `:#                  
                   ::::::`:::::;'  /  /   `#          
                """);
        out.println("RIP, somebody has disconnected\ngame is currently in stand-by (lobby#" + gameId + ")");
        playersConnections.forEach((key, value) -> out.println(key + " : " + value));
    }

    @Override
    public void promptGame(GameDTO game) {
        gamePrompt.prompt(game);
    }

    @Override
    public void promptEvent(List<GameEvent> eventList) {
        eventList.forEach(eventHandler::acceptEvent);
    }

    @Override
    public void promptText(String text) {
        out.println(text);
    }

    @Override
    public void promptError(String errorMsg) {
        promptText("!!ERROR!! : " + errorMsg);
    }

    @Override
    public void setPlayerName(String name) {
        this.name = name;
        gamePrompt.setPlayerName(name);
    }

    @Override
    public void promptLobbiesStatus(Set<String> activeLobbies, Set<String> loadableLobbies) {

        out.println("lobbies changed");
        promptLobbies(activeLobbies, loadableLobbies);
    }

    private void promptLobbies(Set<String> activeLobbies, Set<String> loadableLobbies) {
        out.println("available active lobbies are");
        if (activeLobbies.isEmpty()) {
            out.println("-- no active lobbies --");
        }
        activeLobbies.forEach(out::println);
        if (!loadableLobbies.isEmpty()) {
            out.println("loadable lobbies are");
            loadableLobbies.forEach(out::println);
        }
    }
}
