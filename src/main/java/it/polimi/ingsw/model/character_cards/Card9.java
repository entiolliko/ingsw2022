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

public class Card9 extends CharacterCard {
    private int cost = 3;
    private boolean played;

    public Card9() {
        played = false;
    }

    public void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException {
        super.check();

        flushParams(islandIndex, tokensToAdd, tokensToRemove);

        board.getDashBoards().get(playerID).removeCoins(cost);
        board.getInfluenceHandler().shutDownProfessor(toActivate);

        if (!played) {
            played = true;
            cost = cost + 1;
        }
        this.currentlyActive = true;
        board.notifyObservers(new CCActivatedEvent(this.getCardName(), toActivate + " tokens are worthless this turn, they stole jam from the frigde"));
    }

    private void flushParams(int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) {
        if (islandIndex == -1111 || tokensToAdd != null || tokensToRemove != null)
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
        Card9 card9 = (Card9) o;
        return Objects.equals(classname, card9.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classname);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        CharacterCardDTO simpleCard = new CharacterCardDTO();
        simpleCard.setCardName("Card9");
        simpleCard.setCost(this.cost);
        simpleCard.setActive(currentlyActive);
        simpleCard.setTokens(TokenCollection.newEmptyCollection().getMap());
        simpleCard.setEffect("choose a colour: that colour's influence is ignored for this turn");
        gameDTO.getCharacterCards().add(simpleCard);
    }
}
