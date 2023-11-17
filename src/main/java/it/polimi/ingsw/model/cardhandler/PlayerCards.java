package it.polimi.ingsw.model.cardhandler;

import it.polimi.ingsw.controller.data_transfer_objects.DashboardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * class to Handle Apprentice Cards that emulates Player actions
 */
public class PlayerCards implements Simplifiable {

    //attributes are friendly to make code slimmer when testing
    String playerID;
    boolean hasPlayedCard;
    List<ApprenticeCard> availableCards;
    Deque<ApprenticeCard> playedCards;

    /**
     * constructor
     *
     * @param playerID       = String that identifies a player
     * @param availableCards = List of Starting cards
     */
    public PlayerCards(@NotNull String playerID, @NotNull List<ApprenticeCard> availableCards) {

        this.playerID = playerID;
        this.availableCards = availableCards;
        this.playedCards = new ArrayDeque<>();
        this.hasPlayedCard = false;
    }

    /**
     * static method to create all ApprenticeCards
     *
     * @return a default List of Apprentice cards
     */
    @NotNull
    public static List<ApprenticeCard> createApprenticeCards() {
        List<ApprenticeCard> returnValue = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            returnValue.add(new ApprenticeCard(i, i, i / 2 + i % 2));
        }
        return returnValue;
    }

    /**
     * @param cardID = Integer that identifies a card
     * @throws AlreadyPlayedCardException     thrown if a players try to play a card already played, or tries to play two cards in a turn
     * @throws OutOfBoundIntegerCardException thrown if cardID given is out o the cardID bound
     */
    public void playCard(@NotNull Integer cardID) throws AlreadyPlayedCardException, OutOfBoundIntegerCardException {

        //check if card is in bound
        if (cardID > availableCards.size() + playedCards.size() || cardID < 1)
            throw new OutOfBoundIntegerCardException();
        if (hasPlayedCard)
            throw new AlreadyPlayedCardException("a card was already played this turn by the player: " + this.playerID);
        //check if the integer matches any
        if (availableCards.stream().anyMatch(card -> card.cardID.equals(cardID))) {
            ApprenticeCard toMove = availableCards.stream().filter(card -> card.cardID.equals(cardID))
                    .findFirst()
                    //this orElse is here just to return the object and not the optional
                    .orElse(null);
            this.playedCards.add(toMove);
            this.availableCards.remove(toMove);
            this.hasPlayedCard = true;
        } else {
            throw new AlreadyPlayedCardException("a card with the same card Id was played by the player: " + this.playerID);
        }
    }

    public String getPlayerID() {
        return playerID;
    }

    /**
     * method to retrieve the last card played, can be NULL if called before playing any card
     *
     * @return the last played card on the stack
     */
    //can retrieve NULL when called before playing any card
    @Nullable
    public ApprenticeCard getLastCard() {
        return this.playedCards.getLast();
    }

    /**
     * method to set class for next turn, set hasPlayedCard to false
     */
    public void prepareForNextTurn() {
        this.hasPlayedCard = false;
    }

    /**
     * method to see if Player has already played a card this turn
     *
     * @return a copy of hasPlayedCard
     */
    @NotNull
    public Boolean hasPlayedCard() {
        return this.hasPlayedCard;
    }

    /**
     * a method to return a List copy of playedCards
     *
     * @return a List that is a copy of playedCards
     */
    public List<ApprenticeCard> getPlayedCards() {
        return new ArrayList<>(this.playedCards);
    }

    /**
     * a method to return a copy of availableCards
     *
     * @return a copy o availableCards
     */
    @NotNull
    public List<ApprenticeCard> getAvailableCards() {
        return new ArrayList<>(this.availableCards);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCards that = (PlayerCards) o;
        return hasPlayedCard == that.hasPlayedCard && Objects.equals(playerID, that.playerID) && Objects.equals(availableCards, that.availableCards) && Objects.equals(new ArrayList<>(playedCards), new ArrayList<>(that.playedCards));
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, hasPlayedCard, availableCards, playedCards);
    }

    @Override
    public String toString() {
        String constant = "\n\t\t\t";
        return "PlayerCards{" + constant +
                "playerID='" + playerID + '\'' + constant +
                ", hasPlayedCard=" + hasPlayedCard + constant +
                ", availableCards=" + availableCards + constant +
                ", playedCards=" + playedCards + constant +
                '}';
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        DashboardDTO simpleDashBoard = gameDTO.getDashboards().get(playerID);
        List<Integer> cardIndexes = availableCards.stream()
                .map(card -> card.cardID)
                .collect(Collectors.toList());
        simpleDashBoard.setTheHand(cardIndexes);
        List<Integer> playedCardsIndexes = playedCards.stream()
                .map(card -> card.cardID)
                .collect(Collectors.toList());
        simpleDashBoard.setPlayedCards(new ArrayDeque<>(playedCardsIndexes));
        simpleDashBoard.setHasPlayedCard(this.hasPlayedCard);
    }
}

