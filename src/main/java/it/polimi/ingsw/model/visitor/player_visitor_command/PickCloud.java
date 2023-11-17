package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.EmptyException;
import it.polimi.ingsw.model.game_event.game_events.CloudToEntranceHallEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PickCloud extends GsonablePrototype implements PlayerVisitorCommand {

    private final String playerID;
    private final Integer cloudID;

    public PickCloud(String playerID, Integer cloudID) {
        super();
        this.cloudID = cloudID;
        this.playerID = playerID;
    }

    @Override
    public void visit(@NotNull Board board) throws EmptyException, ReloadGameException {
        if (board.getBag().isEmpty() && board.getClouds().stream().mapToInt(Cloud::size).allMatch(size -> size == 0)) {
            board.notifyObservers(new CloudToEntranceHallEvent(cloudID, playerID, TokenCollection.newEmptyCollection().getMap()));
            return;
        }

        Cloud pickedCloud = board.getClouds().get(cloudID);
        TokenCollection cloudContent = pickedCloud.removeTokens();
        if (cloudContent.isEmpty()) throw new EmptyException("this cloud has already been picked; saved you from your own self fam ;)");
        board.getDashBoards().get(playerID).moveToEntranceHall(cloudContent);
        board.notifyObservers(new CloudToEntranceHallEvent(cloudID, playerID, cloudContent.getMap()));
    }

    @Override
    public String getPlayerID() {
        return playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PickCloud pickCloud = (PickCloud) o;
        return Objects.equals(playerID, pickCloud.playerID) && Objects.equals(cloudID, pickCloud.cloudID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, cloudID);
    }
}
