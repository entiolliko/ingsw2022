package it.polimi.ingsw.model.cardhandler;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.ModelEventCreator;
import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.IllegalApprenticeCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import it.polimi.ingsw.model.game_event.game_events.PlayedApprenticeCardEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * class to handle ApprenticeCards
 */
public class CardHandler extends ModelEventCreator implements Simplifiable {
    private static final int BONUS_MOTHER_NATURE_MOVEMENT = 2;

    Map<String, PlayerCards> playersOnTheTable;
    String winningPlayer;
    String firstToPlay;
    List<PlayerCards> playersOrder;
    String bonusReceiver;

    /**
     * constructor
     *
     * @param playersID List of String containing players identifiers
     */
    public CardHandler(@NotNull List<String> playersID) {
        playersOnTheTable = new HashMap<>();
        for (String playerID : playersID) {
            this.playersOnTheTable.put(playerID, new PlayerCards(playerID, PlayerCards.createApprenticeCards()));
        }

        this.winningPlayer = null;
        this.firstToPlay = playersID.get(new Random().nextInt(playersID.size()));
        playersOrder = new ArrayList<>();
        bonusReceiver = null;
    }

    /**
     * @param playerID String that identifies a player
     * @param cardID   Integer that identifies an ApprenticeCard
     * @throws IllegalApprenticeCardException thrown when a player tries to play a card whose cardID is already been played in the same turn by another player, only i he has other options in hand to play
     * @throws AlreadyPlayedCardException     thrown if a players try to play a card already played, or tries to play two cards in a turn
     * @throws OutOfBoundIntegerCardException thrown if cardID given is out o the cardID bound
     */
    public void playCard(@NotNull String playerID, @NotNull Integer cardID) throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {

        if (!canPlayCard(playerID, cardID)) throw new IllegalApprenticeCardException("you cannot play this card now");
        playersOnTheTable.get(playerID).playCard(cardID);
        if (Objects.isNull(winningPlayer)) this.winningPlayer = playerID;
        else if (this.playersOnTheTable
                .values()
                .stream()
                .filter(playerCards -> !playerCards.getPlayerID().equals(playerID))
                .filter(PlayerCards::hasPlayedCard)
                .allMatch(playercards -> playercards.getLastCard().orderValue > cardID)) {
            this.winningPlayer = playerID;
        }
        playersOrder.add(playersOnTheTable.get(playerID));
        this.notifyObservers(new PlayedApprenticeCardEvent(playerID, cardID));
    }

    /**
     * help method to identify if IllegalApprenticeCard should be thrown
     *
     * @param playerID String that identifies a player
     * @param cardID   Integer that identifies an ApprenticeCard
     * @return true if IllegalApprenticeException should not be thrown otherwise false
     */
    @NotNull
    Boolean canPlayCard(String playerID, Integer cardID) {

        List<ApprenticeCard> cardsPlayedThisTurn = playersOnTheTable.values()
                .stream()
                .filter(PlayerCards::hasPlayedCard)
                .map(PlayerCards::getLastCard)
                .toList();

        Stream<ApprenticeCard> playerAvailableCards = playersOnTheTable.get(playerID).getAvailableCards().stream();

        if (cardsPlayedThisTurn.stream().anyMatch(i -> i.cardID.equals(cardID))) {

            //checks if all the player cards are not playable, if so pass
            if (playerAvailableCards.allMatch(i -> cardsPlayedThisTurn.stream().anyMatch(j -> j.cardID.equals(i.cardID)))) {
                return true;
            }

            // make this pass to raise the correct exception into PlayerCards
            else return this.playersOnTheTable.get(playerID).hasPlayedCard() ||
                    this.playersOnTheTable.get(playerID).playedCards
                            .stream()
                            .mapToInt(i -> i.cardID)
                            .anyMatch(i -> i == cardID);
        }
        return true;
    }

    public String getWinningPlayer() {
        return this.winningPlayer;
    }

    /**
     * method to set class for next turn, calls PlayerCards.prepareForNextTurn() for each player
     */
    public void prepareForNextRound() {
        playersOnTheTable.values().forEach(PlayerCards::prepareForNextTurn);
        this.firstToPlay = winningPlayer;
        this.winningPlayer = null;
        playersOrder = new ArrayList<>();
        bonusReceiver = null;
    }

    public void prepareForNextTurn(){
        bonusReceiver = null;
    }

    /**
     * method to get last played card of given player
     *
     * @param playerID String that identifies a player
     * @return last played card of playerID
     */
    @Nullable
    public ApprenticeCard lastPlayedCard(@NotNull String playerID) {
        return playersOnTheTable.get(playerID).getLastCard();
    }

    public int lastPlayedCardMovement(@NotNull String playerID) {
        int value = Objects.requireNonNull(this.lastPlayedCard(playerID)).getMovementValue();
        if (playerID.equals(bonusReceiver))
            value += BONUS_MOTHER_NATURE_MOVEMENT;
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardHandler that = (CardHandler) o;
        return Objects.equals(playersOnTheTable, that.playersOnTheTable) && Objects.equals(winningPlayer, that.winningPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playersOnTheTable);
    }

    @Override
    public String toString() {
        String separator = "\n\t\t";
        return "CardHandler{" + separator +
                "playersOnTheTable=" + playersOnTheTable + separator +
                ", winningPlayer='" + winningPlayer + '\'' + separator +
                '}';
    }

    public String getFirstPlayerToMove() {
        return this.firstToPlay;
    }

    public void setBonusReceiver(String bonusReceiver) {
        this.bonusReceiver = bonusReceiver;
    }

    /**
     * Returns the ordered list of players for action phase
     *
     * @return
     */
    public List<String> getOrderedPlayers() {
        return playersOrder.stream()
                .sorted(Comparator.comparingInt(p -> Objects.requireNonNull(p.getLastCard()).orderValue)
                )
                .map(PlayerCards::getPlayerID)
                .toList();
    }

    public List<PlayerCards> showPlayedCards() {
        return playersOnTheTable
                .values()
                .stream()
                .filter(player -> player.hasPlayedCard)
                .toList()
                ;
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        playersOnTheTable.values().forEach(player -> player.fillDTO(gameDTO));
    }

    public boolean gameOverCondition() {
        int returnValue = playersOnTheTable
                .values()
                .stream()
                .mapToInt(playerCards -> playerCards.availableCards.size())
                .sum();
        return returnValue <= 1;
    }
}
