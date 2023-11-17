package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.TokenToIslandEvent;

import java.util.Objects;

public class MoveToIsland extends GsonablePrototype implements PlayerVisitorCommand {
    private final TokenEnum toMove;
    private final Integer islandIndex;
    private final String playerID;

    public MoveToIsland(TokenEnum toMove, Integer islandIndex, String playerID) {
        super();
        this.islandIndex = islandIndex;
        this.toMove = toMove;
        this.playerID = playerID;
    }

    @Override
    public void visit(Board board) {
        board.getDashBoards().get(this.playerID).removeFromEntranceHall(this.toMove);
        board.getIslands().addToken(this.toMove, this.islandIndex);
        board.notifyObservers(new TokenToIslandEvent(playerID, islandIndex, toMove));
    }

    @Override
    public String getPlayerID() {
        return playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveToIsland that = (MoveToIsland) o;
        return toMove == that.toMove && Objects.equals(islandIndex, that.islandIndex) && Objects.equals(playerID, that.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toMove, islandIndex, playerID);
    }
}
