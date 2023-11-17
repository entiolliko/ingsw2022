package it.polimi.ingsw.model.character_cards;

import it.polimi.ingsw.controller.data_transfer_objects.CharacterCardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.character_cards.BagTokenToCardEvent;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CCActivatedEvent;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CardTokenToIslandEvent;

import javax.naming.directory.InvalidAttributesException;
import java.util.Objects;

public class Card1 extends CharacterCard {
    private final TokenCollection tokens;
    private int cost = 1;
    private boolean played;

    public Card1(TokenCollection tokens) throws InvalidAttributesException {
        played = false;
        if (tokens.size() != 4)
            throw new InvalidAttributesException();
        this.tokens = tokens;
        }

    public void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        super.check();

        flushParams(tokensToAdd, tokensToRemove);
        board.getDashBoards().get(playerID).removeCoins(cost);
        this.tokens.popToken(toActivate, 1);
        board.getIslands().addToken(toActivate, islandIndex);
        board.notifyObservers(new CardTokenToIslandEvent(getCardName(), toActivate, islandIndex));
        try {
            TokenEnum extracted = board.getBag().randomTokenPop();
            tokens.addTokens(extracted, 1);
            board.notifyObservers(new BagTokenToCardEvent(extracted , getCardName()));
        } catch (NegativeAmountException e) {
            //Is the check in the game class enough?
        }
        if (!played) {
            played = true;
            cost = cost + 1;
        }
        this.currentlyActive = true;
        board.notifyObservers(new CCActivatedEvent(this.getCardName()));
    }

    private void flushParams(TokenCollection tokensToAdd, TokenCollection tokensToRemove) {
        if (tokensToAdd != null || tokensToRemove != null)
            throw new InvalidCommandException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card1 card1 = (Card1) o;
        return Objects.equals(tokens, card1.tokens);
    }

    @Override
    public String toString() {
        return String.valueOf(currentlyActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @Override
    public String getClassName() {
        return this.classname;
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        CharacterCardDTO simpleCard = new CharacterCardDTO();
        simpleCard.setCardName("Card1");
        simpleCard.setCost(this.cost);
        simpleCard.setActive(currentlyActive);
        simpleCard.setTokens(tokens.getMap());
        simpleCard.setEffect("move one token from this card to your island of choice");
        gameDTO.getCharacterCards().add(simpleCard);
    }
}
