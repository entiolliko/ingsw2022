package it.polimi.ingsw.model.visitor;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import org.jetbrains.annotations.NotNull;

public interface VisitorCommand {

    /**
     * Visits the given game as per the Visitor Command
     * @param game The game to be visited
     * @throws ReloadGameException Thrown when there is an intern error
     */
    default void visit(@NotNull Game game) throws ReloadGameException {
        game.getGameBoard().accept(this);
    }

    /**
     * Visits the Board class as per the Visitor Command
     * @param board The board to be visited
     * @throws ReloadGameException Thrown when there is an intern error
     */
    default void visit(Board board) throws ReloadGameException {
    }

}
