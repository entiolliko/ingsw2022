package it.polimi.ingsw.model.character_cards;

import it.polimi.ingsw.controller.data_transfer_objects.CharacterCardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CCActivatedEvent;

import java.util.Objects;

public class Card4 extends CharacterCard {
    private int cost = 1;
    private boolean played;

    public Card4() {
        played = false;
    }

    public void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        super.check();

        flushParams(toActivate, islandIndex, tokensToAdd, tokensToRemove);

        board.getDashBoards().get(playerID).removeCoins(cost);
        board.getCardHandler().setBonusReceiver(playerID);

        if (!played) {
            played = true;
            cost = cost + 1;
        }

        this.currentlyActive = true;
        board.notifyObservers(new CCActivatedEvent(this.getCardName(), playerID + " can now move Mother Nature up to "+ board.getCardHandler().lastPlayedCardMovement(playerID) +" islands"));
    }

    private void flushParams(TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) {
        if (toActivate != null || islandIndex == -1111 || tokensToAdd != null || tokensToRemove != null)
            throw new InvalidCommandException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card4 card4 = (Card4) o;
        return Objects.equals(classname, card4.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classname);
    }

    @Override
    public String getClassName() {
        return this.classname;
    }

    @Override
    public String toString() {
        return "Card4{" +
                "classname='" + classname + '\'' +
                '}';
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        CharacterCardDTO simpleCard = new CharacterCardDTO();
        simpleCard.setCardName("Card4");
        simpleCard.setCost(this.cost);
        simpleCard.setActive(currentlyActive);
        simpleCard.setTokens(TokenCollection.newEmptyCollection().getMap());
        simpleCard.setEffect("increases mother nature movement by two for this turn");
        gameDTO.getCharacterCards().add(simpleCard);
    }
}
