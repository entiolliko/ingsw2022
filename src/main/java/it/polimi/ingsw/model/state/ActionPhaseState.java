package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;

import java.util.List;

public abstract class ActionPhaseState extends State {
    private final List<String> characterCards = List.of("PlayCharacterCardCommand");

    protected ActionPhaseState(Game game, List<String> players, int currentPlayer) {
        super(game, players);
        this.currentPlayer = currentPlayer;

        if (game != null && game.getTypeOfGame() == TypeOfGame.EXPERT)
            allowedCommands.addAll(characterCards);
    }
}
