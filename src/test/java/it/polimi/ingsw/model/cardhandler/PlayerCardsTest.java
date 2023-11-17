package it.polimi.ingsw.model.cardhandler;

import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCardsTest {

    private PlayerCards mock;
    private String name;
    private List<ApprenticeCard> hand;

    @BeforeEach
    public void _reset() {

        this.hand = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ApprenticeCard new_card = new ApprenticeCard(i, i, i/2 + i%2);
            this.hand.add(new_card);
        }
        this.name = "1";
        this.mock = new PlayerCards(this.name, hand);
    }

    @Test
    void play_card_play_same() {
        assertThrows(AlreadyPlayedCardException.class, this::playCardPlaySame);
        assertThrows(AlreadyPlayedCardException.class, this::playCardPlayOtherPlaySame);
    }

    @Test
    void play_card_out_of_bound() {
        assertThrows(OutOfBoundIntegerCardException.class, () -> this.mock.playCard(-1));
        assertThrows(OutOfBoundIntegerCardException.class, () -> this.mock.playCard(11));
    }

    @Test
    void plays_correct_one() throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        ApprenticeCard moved = this.mock.availableCards.get(0);
        Integer last_size_available = this.mock.availableCards.size();
        Integer last_size_played = this.mock.playedCards.size();
        this.mock.playCard(1);
        assertEquals(moved, this.mock.getLastCard());
        assertEquals(last_size_available, this.mock.availableCards.size() + 1);
        assertEquals(last_size_played, this.mock.playedCards.size() - 1);
    }

    @Test
    void plays_card_has_played() throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        this.mock.playCard(1);
        assertTrue(this.mock.hasPlayedCard);
    }

    @Test
    void correctly_resets() {
        this.mock.prepareForNextTurn();
        assertTrue(!this.mock.hasPlayedCard);
    }

    @Test
    void correctCards(){
        assertEquals(this.mock.availableCards, new PlayerCards(this.name, PlayerCards.createApprenticeCards()).availableCards);
        assertEquals(this.mock.availableCards.get(0).hashCode(), this.mock.availableCards.get(0).hashCode());
    }

    @Test
    void correctGetPlayedCards() throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        assertTrue(this.mock.playedCards.containsAll(new PlayerCards(this.name, PlayerCards.createApprenticeCards()).playedCards));

        assertTrue(this.mock.playedCards.containsAll(new PlayerCards(this.name, PlayerCards.createApprenticeCards()).getPlayedCards()));
        this.mock.playCard(1);
        this.mock.prepareForNextTurn();
        this.mock.playCard(2);
        assertTrue(this.mock.playedCards.containsAll(new PlayerCards(this.name, PlayerCards.createApprenticeCards()).getPlayedCards()));

    }

    private void playCardPlayOtherPlaySame() throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        this.mock.playCard(2);
        this.mock.playCard(3);
        this.mock.playCard(2);
    }

    private void playCardPlaySame() throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        this.mock.playCard(1);
        this.mock.playCard(1);
    }

    @Test
    void equals(){
        assertEquals(this.mock, new PlayerCards(this.name, this.hand));

    }
}