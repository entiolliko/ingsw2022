package it.polimi.ingsw.model.visitor.character_cards;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Objects;

public class PlayCharacterCardCommand implements PlayerVisitorCommand {

    private final String playerID;
    private final String cardName;
    private TokenCollection tokensToAdd = null;
    private TokenCollection tokensToRemove = null;
    private int islandIndex = -1;
    private TokenEnum toActivate = null;
    private String classname;

    public PlayCharacterCardCommand(String cardName, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) {
        this.cardName = cardName;
        this.playerID = playerID;
        this.toActivate = toActivate;
        this.islandIndex = islandIndex;
        this.tokensToAdd = tokensToAdd;
        this.tokensToRemove = tokensToRemove;
        this.classname = this.getClass().getName();
    }

    public static PlayerVisitorCommand fromJson(JsonObject json) {
        System.out.println(json);
        Gson gson = new Gson();

        return new PlayCharacterCardCommand(json.get("cardName").getAsString(),
                json.get("playerID").getAsString(),
                gson.fromJson(json.get("toActivate"), TokenEnum.class),
                json.get("islandIndex").getAsInt(),
                gson.fromJson(json.get("tokensToAdd"), TokenCollection.class),
                gson.fromJson(json.get("tokensToRemove"), TokenCollection.class));
    }

    @Override
    public void visit(Board board) throws ReloadGameException {
        if (!board.getCharacterCardList().stream().map(x -> x.getClass().getSimpleName()).toList().contains(this.cardName))
            throw new ReloadGameException("The board does not contain the card " + this.cardName);
        else
            board.getCharacterCardList().get(getCardIndex(board)).playCard(board, this.playerID, this.toActivate, islandIndex, tokensToAdd, tokensToRemove);
    }

    public String getPlayerID() {
        return this.playerID;
    }

    @Override
    public String getClassName() {
        return this.classname;
    }

    private int getCardIndex(Board board) {
        return board.getCharacterCardList().stream().map(x -> x.getClass().getSimpleName()).toList().indexOf(this.cardName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayCharacterCardCommand that = (PlayCharacterCardCommand) o;
        return islandIndex == that.islandIndex && Objects.equals(playerID, that.playerID) && Objects.equals(cardName, that.cardName) && Objects.equals(tokensToAdd, that.tokensToAdd) && Objects.equals(tokensToRemove, that.tokensToRemove) && toActivate == that.toActivate && Objects.equals(classname, that.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, cardName, tokensToAdd, tokensToRemove, islandIndex, toActivate, classname);
    }
}
