package it.polimi.ingsw.model.character_cards;

import it.polimi.ingsw.controller.data_transfer_objects.CharacterCardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CCActivatedEvent;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CardToEntranceHallEvent;
import it.polimi.ingsw.model.game_event.game_events.character_cards.EntranceHallToCardEvent;

import javax.naming.directory.InvalidAttributesException;
import java.util.Objects;

public class Card7 extends CharacterCard {
    private final TokenCollection tokens;
    private int cost = 1;
    private boolean played;

    public Card7(TokenCollection tokens) throws InvalidAttributesException {
        played = false;
        if (tokens.size() != 6)
            throw new InvalidAttributesException("The collection must have size 6. The size of your collection is : " + tokens.size());
        this.tokens = tokens;
    }

    public void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        super.check();

        flushParams(toActivate, islandIndex);

        if (tokensToAdd == null || tokensToRemove == null || tokensToAdd.size() == 0 || tokensToRemove.size() == 0 || tokensToAdd.size() > 3 || tokensToRemove.size() > 3 || tokensToAdd.size() != tokensToRemove.size())
            throw new ReloadGameException("The given parameters are not correct."); //TODO: Ask about the type of exception

        board.getDashBoards().get(playerID).removeCoins(cost);

        this.tokens.removeFromCollection(tokensToAdd);
        this.tokens.addToCollection(tokensToRemove);
        board.notifyObservers(new EntranceHallToCardEvent(getCardName(),tokensToRemove.getMap(), playerID));

        board.getDashBoards().get(playerID).removeFromEntranceHall(tokensToRemove);
        board.getDashBoards().get(playerID).moveToEntranceHall(tokensToAdd);
        board.notifyObservers(new CardToEntranceHallEvent(playerID, tokensToAdd.getMap(), getCardName()));

        if (!played) {
            played = true;
            cost = cost + 1;
        }
        this.currentlyActive = true;
        board.notifyObservers(new CCActivatedEvent(this.getCardName()));
    }

    private void flushParams(TokenEnum toActivate, int islandIndex) {
        if (toActivate != null || islandIndex == -1111)
            throw new InvalidCommandException();
    }

    @Override
    public String getClassName() {
        return this.classname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card7 card7 = (Card7) o;
        return Objects.equals(classname, card7.classname) && Objects.equals(tokens, card7.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classname, tokens);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        CharacterCardDTO simpleCard = new CharacterCardDTO();
        simpleCard.setCardName("Card7");
        simpleCard.setCost(this.cost);
        simpleCard.setActive(currentlyActive);
        simpleCard.setTokens(tokens.getMap());
        simpleCard.setEffect("trade up to three tokens of yours with just as many tokens in this card");
        gameDTO.getCharacterCards().add(simpleCard);
    }
}
