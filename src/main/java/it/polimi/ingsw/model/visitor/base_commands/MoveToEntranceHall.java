package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.jetbrains.annotations.NotNull;

public class MoveToEntranceHall implements VisitorCommand {

    private String playerID;
    private TokenEnum token;

    public MoveToEntranceHall(String playerID) {
        super();
        this.playerID = playerID;
        token = null;
    }

    public MoveToEntranceHall(String playerID, TokenEnum token) {
        super();
        this.playerID = playerID;
        this.token = token;
    }

    @Override
    public void visit(@NotNull Board board) throws ReloadGameException {
        TokenCollection tokenToAdd;
        if (this.token == null) {
            tokenToAdd = board.getBag().randomPop(1);
            board.getDashBoards().get(this.playerID).moveToEntranceHall(tokenToAdd);
        } else
            board.getDashBoards().get(this.playerID).moveToEntranceHall(token);
    }

    public String getPlayerID() {
        return playerID;
    }
}
