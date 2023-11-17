package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.IllegalApprenticeCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayApprenticeCard extends GsonablePrototype implements PlayerVisitorCommand {

    final String playerID;
    final Integer cardID;

    public PlayApprenticeCard(String playerID, Integer cardID) {
        super();
        this.cardID = cardID;
        this.playerID = playerID;
    }

    @Override
    public void visit(@NotNull Board board) throws ReloadGameException {
        try {
            board.getCardHandler().playCard(this.playerID, this.cardID);
        } catch (IllegalApprenticeCardException | AlreadyPlayedCardException | OutOfBoundIntegerCardException e) {
            throw new ReloadGameException(e.getMessage(), e);
        }
    }

    @Override
    public String getPlayerID() {
        return this.playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayApprenticeCard that = (PlayApprenticeCard) o;
        return Objects.equals(playerID, that.playerID) && Objects.equals(cardID, that.cardID) && Objects.equals(classname, that.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, cardID, classname);
    }
}
