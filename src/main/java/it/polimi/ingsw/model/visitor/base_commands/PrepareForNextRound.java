package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.Board;
import org.jetbrains.annotations.NotNull;

public class PrepareForNextRound implements VisitorCommand {

    @Override
    public void visit(@NotNull Board board) {

        board.getCardHandler().prepareForNextRound();
        board.getProfessors().prepareForNextRound();
        board.getInfluenceHandler().prepareForNextRound();

        try {
            board.accept(new FillClouds());
            board.accept(new PrepareForNextTurn());
        } catch (ReloadGameException e) {
            e.printStackTrace();
        }


    }
}
