package it.polimi.ingsw.client.frontend.command_line;

import it.polimi.ingsw.client.backend.Connector;
import it.polimi.ingsw.client.frontend.ClientOutInterface;
import it.polimi.ingsw.client.frontend.command_line.game_prompt.PrettyStringFormer;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.client_requests.CreateLobbyReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.JoinLobbyReq;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.*;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LineInterpreter {
    private final Connector connector;
    private final Scanner scanner;
    private final ClientOutInterface screen;
    private final CharacterCardRequestBuilder characterCardRequestBuilder;
    private String name;

    public LineInterpreter(Connector connector, InputStream inputStream, ClientOutInterface screen) {
        this.connector = connector;
        this.scanner = new Scanner(inputStream);
        this.screen = screen;
        this.characterCardRequestBuilder = new CharacterCardRequestBuilder(scanner, screen);
        this.name = "none";
    }

    public void setName(String name) {
        this.name = name;
        screen.setPlayerName(name);
    }

    public void startListening() {
        new Thread(this::listen).start();
    }

    private void listen() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                issueRequest(line);
            }
            catch (RuntimeException e) {
                screen.promptText("care for the syntax, if needed type help for guidance");
            }

        }
    }

    private void issueRequest(String line) {
        String[] splitString = line.split(" ");
        String firstWord = splitString[0];
        switch (firstWord) {

            // Server access
            case "js" -> { //join server as js ip:port
                String address = line.split(" ")[1];
                String ip = address.split(":")[0];
                int port = Integer.parseInt(address.split(":")[1]);
                try {
                    connector.connectToServer(ip, port);
                } catch (IOException e) {
                    screen.promptText("socket could not connect");
                }
            }
            case "q" -> // quit
                    connector.quit();


            // Lobby access
            case "cl" -> { // create lobby as cl playerName numOfPlayers N/E
                this.setName(line.split(" ")[1]);
                connector.sendRequest(new CreateLobbyReq(splitString[1], Integer.parseInt(splitString[2]),
                        TypeOfGame.EXPERT.equals(TypeOfGame.valueOf(splitString[3].toUpperCase())) ? TypeOfGame.EXPERT : TypeOfGame.NORMAL));
            }

            case "jl" -> { // join lobby as jl playerName lobbyID
                this.setName(line.split(" ")[1]);
                connector.sendRequest(new JoinLobbyReq(splitString[1], splitString[2]));
            }
            case "pac" -> { // playApprenticeCard as pac cardNum
                ClientRequest request = new PlayApprenticeCardReq(Integer.parseInt(splitString[1]));
                connector.sendRequest(request);
            }

            case "pcc" -> { // playCharacterCard as pac cardName
                ClientRequest request =
                        switch (Integer.parseInt(splitString[1])) {
                            case 2, 4, 6, 8 -> characterCardRequestBuilder.simpleCard(splitString[1]);
                            case 9, 11 -> characterCardRequestBuilder.tokenCard(splitString[1], TokenEnum.valueOf(splitString[2].toUpperCase()));
                            case 1 -> characterCardRequestBuilder.tokenIslandCard(splitString[1],TokenEnum.valueOf(splitString[2].toUpperCase()), Integer.parseInt(splitString[3]));
                            case 7 -> characterCardRequestBuilder.doubleCollectionCard(splitString[1]);
                            default -> throw new IllegalArgumentException("Invalid Character Card ID");
                        };
                connector.sendRequest(request);
            }

            case "mtsh" -> { // moveToStudyHall as mtsh colour
                ClientRequest moveToStudyHall = new MoveToStudyHallReq(TokenEnum.valueOf(splitString[1].toUpperCase()));
                connector.sendRequest(moveToStudyHall);
            }
            case "mti" -> { // moveToIsland as mti colour islandIndex
                ClientRequest moveToIsland = new MoveToIslandReq(TokenEnum.valueOf(splitString[1].toUpperCase()), Integer.parseInt(splitString[2]));
                connector.sendRequest(moveToIsland);
            }
            case "mmn" -> { // moveMotherNature as mmn movementValue
                ClientRequest moveMMN = new MoveMotherNatureReq(Integer.parseInt(splitString[1]));
                connector.sendRequest(moveMMN);
            }
            case "pc" -> { //pick cloud as pc cloudIndex
                ClientRequest pickCloud = new PickCloudReq(Integer.parseInt(splitString[1]));
                connector.sendRequest(pickCloud);
            }
            case "ueue" -> screen.promptText(PrettyStringFormer.supremeLogo());
            case "help" -> screen.promptText(HelpLegend.create());
            default -> screen.promptText("a command like that does not exist read the docs :), type help for that");
        }
    }
}

class HelpLegend{
    private HelpLegend(){}
    private static String legend;
    static{
        legend = """
                 /$$$$$$$$           /$$                       /$$                               /$$                                                     /$$
                | $$_____/          |__/                      | $$                              | $$                                                    | $$
                | $$        /$$$$$$  /$$  /$$$$$$  /$$$$$$$  /$$$$$$   /$$   /$$  /$$$$$$$      | $$        /$$$$$$   /$$$$$$   /$$$$$$  /$$$$$$$   /$$$$$$$
                | $$$$$    /$$__  $$| $$ |____  $$| $$__  $$|_  $$_/  | $$  | $$ /$$_____/      | $$       /$$__  $$ /$$__  $$ /$$__  $$| $$__  $$ /$$__  $$
                | $$__/   | $$  \\__/| $$  /$$$$$$$| $$  \\ $$  | $$    | $$  | $$|  $$$$$$       | $$      | $$$$$$$$| $$  \\ $$| $$$$$$$$| $$  \\ $$| $$  | $$
                | $$      | $$      | $$ /$$__  $$| $$  | $$  | $$ /$$| $$  | $$ \\____  $$      | $$      | $$_____/| $$  | $$| $$_____/| $$  | $$| $$  | $$
                | $$$$$$$$| $$      | $$|  $$$$$$$| $$  | $$  |  $$$$/|  $$$$$$$ /$$$$$$$/      | $$$$$$$$|  $$$$$$$|  $$$$$$$|  $$$$$$$| $$  | $$|  $$$$$$$
                |________/|__/      |__/ \\_______/|__/  |__/   \\___/   \\____  $$|_______/       |________/ \\_______/ \\____  $$ \\_______/|__/  |__/ \\_______/
                                                                       /$$  | $$                                     /$$  \\ $$                             \s
                                                                      |  $$$$$$/                                    |  $$$$$$/                             \s
                                                                       \\______/                                      \\______/                              \s                                                      
                A legendary legend for LEGEN... wait for it ... DARY bros
                             _      _        _             _  \s
                            (_)    | |      | |           | | \s
                  __ _ _   _ _  ___| | _____| |_ __ _ _ __| |_\s
                 / _` | | | | |/ __| |/ / __| __/ _` | '__| __|
                | (_| | |_| | | (__|   <\\__ \\ || (_| | |  | |_\s
                 \\__, |\\__,_|_|\\___|_|\\_\\___/\\__\\__,_|_|   \\__|
                    | |                                       \s
                    |_|                                       \s 
                    
                this legend is divided by command category, you will find 2 categories:
                    - connection : the commands needed to connect to the server and join/create a game
                    - game : commands needed to play the game
                
                the overview of the steps to create or join a game is the following:
                    - connecting to the server
                    - create / join a lobby
                    - wait for everyone to connect (the game starts when lobby reach max number of players)
                    - have fun :)
                
                for the commands syntax the field specified with [] are needed for a correct execution while 
                the fields with () are optional
                
                every command has at least a working example at the end of the section, use it :)
                

                                                        _   _            \s
                                                       | | (_)           \s
                   ___ ___  _ __  _ __  _ __   ___  ___| |_ _  ___  _ __ \s
                  / __/ _ \\| '_ \\| '_ \\| '_ \\ / _ \\/ __| __| |/ _ \\| '_ \\\s
                 | (_| (_) | | | | | | | | | |  __/ (__| |_| | (_) | | | |
                  \\___\\___/|_| |_|_| |_|_| |_|\\___|\\___|\\__|_|\\___/|_| |_|
                  
                the commands before entering the game
                
                TYPYCAL ORDER OF CONNECTION COMMANDS
                create a lobby: js -> cl
                join a lobby: js -> jl
                
                COMMANDS:
                - js -
                
                description:    command to connect to a server
                
                info needed:    [server_ip]
                                [server_port]
                
                syntax:         js [server_ip]:[server_port]
                example:        js localhost:9001
                
                --------------------------------------------------------------------------------
                
                - cl -
                
                description:    command to create a new lobby, the lobby 
                                will automatically start the game when 
                                [max_num_of_players] is reached
                                
                info needed:    [game_mode] that could be either expert or normal (see game rules for a more detailed explanation)
                                [player_name] the name you want as a player
                                [max_num_of_players] how many players (can be only 2,3 or 4)
                                
                syntax:         cl [player_name] [max_num_of_players] [game_mode]
                example:        cl mario 2 normal
                                cl mario 2 expert
                                cl mario 3 expert
                                cl mario 4 expert
                                
                
                --------------------------------------------------------------------------------
                
                - jl -
                
                description:    command to join an existing lobby, the lobby 
                                will automatically start the game when 
                                [max_num_of_players] is reached  (see - cl - for further explanation)
                                this command is also used to reconnect to a game in case of client and/or server disconnection, the syntax remains the same
                                
                info needed:    [player_name] the name you want as a player
                                [lobby_id] the number of the lobby you want to connect to
                                
                syntax:         jl [player_name] [lobby_id]
                example:        jl mario 25 
                
                --------------------------------------------------------------------------------
                
                - q -
                
                description:    command to quit the game and disconnect from the server
                                
                info needed:    /
                                
                syntax:         q
                example:        q
                                            \s
                   __ _  __ _ _ __ ___   ___\s
                  / _` |/ _` | '_ ` _ \\ / _ \\
                 | (_| | (_| | | | | | |  __/
                  \\__, |\\__,_|_| |_| |_|\\___|
                   __/ |                    \s
                  |___/                     \s
                  
                the commands to play the game
                
                TYPYCAL ORDER OF GAME COMMANDS IN A TURN
                normal mode: pac -> mtsh/mti -> mmn -> pc
                expert mode: pac -> mtsh/mti -> pcc -> mmn -> pc
                
                --------------------------------------------------------------------------------
                
                - pac -
                
                description:    command to play an apprentice card
                                           
                info needed:    [apprentice_card_id] the value of the apprentice card you want to play
                
                states allowed: PlayApprenticeCardState
                                
                syntax:         pac [apprentice_card_id]
                example:        pac 1
                                pac 4
                                pac 5                 
                --------------------------------------------------------------------------------
                
                - mti -
                
                description:    command to move a token from the entrance hall to an island
                                           
                info needed:    [token_name] the color of the token you want to move
                                [island_index] the index of the island you want the token moved to
                
                states allowed: PlaceTokensState
                                
                syntax:         mti [token_name] [island_index]
                example:        mti GREEN 1
                                mti VIOLET 5
                                mti GREEN 6 
                --------------------------------------------------------------------------------
                
                - mtsh -
                
                description:    command to move a token from the entrance hall to the study hall
                                           
                info needed:    [token_name] the color of the token you want to move
                
                states allowed: PlaceTokensState
                                
                syntax:         mtsh [token_name] 
                example:        mtsh GREEN 
                                mtsh VIOLET 
                                mtsh GREEN
                --------------------------------------------------------------------------------
                
                - mmn -
                
                description:    command to move mother nature
                                                           
                info needed:    [amount] the amount of movement you want mother nature to move
                
                states allowed: MoveMotherNatureState
                                
                syntax:         mmn [amount]
                example:        mmn 1
                                mmn 2
                                mmn 8

                further info: the amount has to be a number between 1 and the movement value of the card played with pac      
                --------------------------------------------------------------------------------
                
                - pc -
                
                description:    command to move tokens from a cloud to the entrance hall
                                           
                info needed:    [cloud_index] the index of the cloud you want to take the tokens from
                
                states allowed: PickCloudState
                                
                syntax:         pc [cloud_index]
                example:        pc 0
                                pc 1
                --------------------------------------------------------------------------------
                
                - pcc -
                
                
                
                description:    command to play a character card 
                                there are 3 main types of character cards:
                                    - simple : only the id of the character card is needed
                                    - token : need to specify which token
                                    - token and isle : need to specify a token and an island index
                                    - two set of tokens: needs two sets of tokens (it has a interactive submenu)
                                note that the examples specify the cards that belong to the type
                                for example [character_card_id] = 1 -> you are calling Card1
                                
                    - simple -
                    info needed: [character_card_id] the id of the card
                    syntax:     pcc [character_card_id]
                    example:    pcc 2
                                pcc 4
                                pcc 6
                                pcc 8
                    
                    - token - 
                    info needed:[character_card_id] the id of the card
                                [token_colour] the token you want to move
                    syntax:     pcc [character_card_id] [token_colour]
                    example:    pcc 9 GREEN
                                pcc 11 VIOLET
                    
                    - token and isle -
                    info needed:[character_card_id] the id of the card
                                [token_colour] the token you want to move
                                [island_index] the id of the island
                                
                    syntax:     pcc [character_card_id] [token_colour] [island_index]
                    example:    pcc 1 GREEN 2
                    
                    - two sets of tokens -
                    info needed:[character_card_id] the id of the card
                                [tokens_colour] a set of tokens
                                [tokens_colour1] a set of tokens
                                
                    syntax:     pcc [character_card_id]
                    example:    pcc 7
                    this card then opens a submenu that will guide you on how to specify the [tokens_colour]
                        example: RED YELLOW GREEN                     
                                 BLUE GREEN
                                 VIOLET
                --------------------------------------------------------------------------------

                """;
    }
    public static String create(){
       return legend;
    }
}