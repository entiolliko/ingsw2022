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
import it.polimi.ingsw.model.game_event.game_events.character_cards.CardTokenToStudyHallEvent;

import javax.naming.directory.InvalidAttributesException;
import java.util.Objects;

public class Card11 extends CharacterCard {
    private final TokenCollection tokens;
    private int cost = 2;
    private boolean played;

    public Card11(TokenCollection tokens) throws InvalidAttributesException {
        played = false;
        if (tokens == null || tokens.size() != 4)
            throw new InvalidAttributesException();
        this.tokens = tokens;
    }

    public void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        super.check();
        flushParams(islandIndex, tokensToAdd, tokensToRemove);

        if (toActivate == null)
            throw new InvalidCommandException();

        board.getDashBoards().get(playerID).removeCoins(cost);

        this.tokens.popToken(toActivate, 1);

        board.getDashBoards().get(playerID).moveToStudyHall(toActivate);
        board.notifyObservers(new CardTokenToStudyHallEvent(getCardName(), toActivate, playerID));

        try {
            TokenEnum removed = board.getBag().randomTokenPop();
            this.tokens.addTokens(removed, 1);
            board.notifyObservers(new BagTokenToCardEvent(removed, getCardName()));
        } catch (NegativeAmountException e) {
            //Is the check in the game class enough?
        }

        if (!played) {
            played = true;
            cost = cost + 1;
        }
        this.currentlyActive = true;
        board.notifyObservers(new CCActivatedEvent(this.getCardName()));
        board.getProfessors().updateProfessors(board.getDashBoards());
    }

    public void flushParams(int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        if (islandIndex == -1111 || tokensToAdd != null || tokensToRemove != null)
            throw new ReloadGameException("invalid arguments in Card11!");
    }

    @Override
    public String toString() {
        return String.valueOf(currentlyActive);
    }

    @Override
    public String getClassName() {
        return this.classname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card11 card11 = (Card11) o;
        return Objects.equals(classname, card11.classname) && Objects.equals(tokens, card11.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classname, tokens);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        CharacterCardDTO simpleCard = new CharacterCardDTO();
        simpleCard.setCardName("Card11");
        simpleCard.setCost(this.cost);
        simpleCard.setActive(currentlyActive);
        simpleCard.setTokens(tokens.getMap());
        simpleCard.setEffect("add one of this card's tokens to your study hall");
        gameDTO.getCharacterCards().add(simpleCard);
    }

}
