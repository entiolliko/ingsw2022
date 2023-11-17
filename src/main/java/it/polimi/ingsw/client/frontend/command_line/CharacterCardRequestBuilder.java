package it.polimi.ingsw.client.frontend.command_line;

import it.polimi.ingsw.client.frontend.ClientOutInterface;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.character_cards.PlayCharacterCardReq;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacterCardRequestBuilder {

    private final Scanner scanner;
    private final ClientOutInterface screen;


    public CharacterCardRequestBuilder(Scanner scanner, ClientOutInterface screen) {
        this.scanner = scanner;
        this.screen = screen;
    }

    public ClientRequest simpleCard(String cardName) {
        return new PlayCharacterCardReq("Card" + cardName);
    }


    public ClientRequest tokenCard(String cardName, TokenEnum token) {
        return new PlayCharacterCardReq("Card" + cardName, token);
    }

    public ClientRequest tokenIslandCard(String cardName, TokenEnum token, int islandIndex) {
        return new PlayCharacterCardReq("Card" + cardName, token, islandIndex);
    }

    public ClientRequest doubleCollectionCard(String cardName) {
        screen.promptText("Select 3 tokens from the card (e.g. RED YELLOW GREEN)");
        String[] toTake = scanner.nextLine().split(" ", 3);
        screen.promptText("select 3 tokens to give away (e.g. RED YELLOW GREEN)");
        String[] toGive = scanner.nextLine().split(" ", 3);
        DebugLogger.log("to take from card: " + toMap(toTake), Level.INFO);
        DebugLogger.log("to give to card: " + toMap(toGive), Level.INFO);
        return new PlayCharacterCardReq("Card" + cardName, toMap(toTake), toMap(toGive));
    }

    private Map<TokenEnum, Integer> toMap(String[] toConvert) {
        return Stream.of(TokenEnum.values())
                .collect(Collectors.toMap(token -> token, token ->
                                Stream.of(toConvert)
                                        .map(str -> TokenEnum.valueOf(str.toUpperCase()))
                                        .filter(tk -> tk.equals(token))
                                        .mapToInt(x -> 1)
                                        .sum()
                        )
                );
    }
}
