package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import org.jetbrains.annotations.NotNull;

public class PrepareForNextTurn implements VisitorCommand {
    @Override
    public void visit(@NotNull Board board) {
        board
                .getCharacterCardList()
                .forEach(card -> card.reset(board));
        board.getProfessors().prepareForNextTurn();
        board.getInfluenceHandler().prepareForNextTurn();
        board.getCardHandler().prepareForNextTurn();
    }
}
