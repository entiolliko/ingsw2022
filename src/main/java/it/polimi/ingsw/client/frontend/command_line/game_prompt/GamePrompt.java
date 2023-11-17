package it.polimi.ingsw.client.frontend.command_line.game_prompt;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.debug_utility.DebugLogger;

import java.io.PrintWriter;
import java.util.logging.Level;

public class GamePrompt {
    private final PrintWriter out;
    private final PrettyStringFormer prettyStringFormer;
    private String playerName;
    private final String separator = """
              .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.      .--.    
            :::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\::::::::.\\:::
            '      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'      `--'
            """;

    public GamePrompt(PrintWriter printWriter) {
        this.out = printWriter;
        this.prettyStringFormer = new PrettyStringFormer();
    }

    public void prompt(GameDTO game) {
        DebugLogger.log("game is being prompted, State : " + game.getGamePhase(), Level.INFO);
        promptMainGame(game);
        out.println("current State : " + game.getGamePhase());
        out.println(game.getCurrentPlayer() + "'s turn" + (game.getCurrentPlayer().equals(playerName) ? "(you)" : ""));
    }

    private void promptMainGame(GameDTO game) {
        out.println(separator);
        out.println("tokens left in the bag: " + game.getBagSize());
        showCharCards(game);
        out.println(prettyStringFormer.cloudsFormat(game));
        out.println(prettyStringFormer.islandsRep(game));
        out.println(prettyStringFormer.dashBoardsRep(game));
        out.println(prettyStringFormer.cardTableRep(game));
    }

    private void showCharCards(GameDTO game) {
        if (!game.getCharacterCards().isEmpty()) {
            game.getCharacterCards().forEach(card -> out.println(card.getCardName() + ": " + card.getEffect()));
            out.println(prettyStringFormer.characterCardsFormat(game));
        }
    }

    public void setPlayerName(String name) {
        this.playerName = name;
        prettyStringFormer.setYourName(name);
    }
}
