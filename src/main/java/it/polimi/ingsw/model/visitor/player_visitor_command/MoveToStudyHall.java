package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Objects;

public class MoveToStudyHall extends GsonablePrototype implements PlayerVisitorCommand {
    private final String playerID;
    private final TokenEnum toMove;

    public MoveToStudyHall(String playerID, TokenEnum toMove) {
        super();
        this.playerID = playerID;
        this.toMove = toMove;
    }

    @Override
    public void visit(Board board) throws ReloadGameException {
        board.getDashBoards().get(this.playerID).moveToStudyHallFromEntrance(this.toMove);
        board.getProfessors().updateProfessors(board.getDashBoards());
    }

    @Override
    public String getPlayerID() {
        return playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveToStudyHall that = (MoveToStudyHall) o;
        return Objects.equals(playerID, that.playerID) && toMove == that.toMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, toMove);
    }
}
