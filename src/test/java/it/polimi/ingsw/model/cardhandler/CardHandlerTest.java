package it.polimi.ingsw.model.cardhandler;

import it.polimi.ingsw.model.dashboard.exceptions.AlreadyPlayedCardException;
import it.polimi.ingsw.model.dashboard.exceptions.IllegalApprenticeCardException;
import it.polimi.ingsw.model.dashboard.exceptions.OutOfBoundIntegerCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CardHandlerTest {
    private List<String> playersID;
    private CardHandler testCardHandler;

    @BeforeEach
    public void setup(){
        this.playersID = Arrays.asList("A", "B", "C", "D");
        this.testCardHandler = new CardHandler(this.playersID);
    }

    @Test
    void correctConstructor(){

        //check if constructor correctly initialize players ID
        assertTrue(this.testCardHandler.playersOnTheTable.keySet().containsAll(this.playersID));
        assertTrue(this.playersID.containsAll(this.testCardHandler.playersOnTheTable.keySet()));

        //check values
        assertEquals(this.testCardHandler.playersOnTheTable.values().size(), this.playersID.size());
        assertTrue(this.testCardHandler.playersOnTheTable
                .values()
                .stream()
                .map(PlayerCards::getClass)
                .allMatch(PlayerCards.class::equals));

    }

    @Test
    void throwsAlreadyPlayedCardException () throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        testCardHandler.playCard("A", 1);
        assertTrue(testCardHandler.playersOnTheTable.get("A").hasPlayedCard());

        assertThrows(AlreadyPlayedCardException.class,()->testCardHandler.playCard("A", 2));
        testCardHandler.prepareForNextRound();
        assertThrows(AlreadyPlayedCardException.class,()->testCardHandler.playCard("A", 1));

    }
    @Test
    void throwsOutOfBoundIntegerException () throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        assertThrows(OutOfBoundIntegerCardException.class,()->testCardHandler.playCard("B", 0));
    }

    @Test
    void throwsIllegalApprenticeException() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        testCardHandler.playCard("A", 3);
        assertThrows(IllegalApprenticeCardException.class,()->testCardHandler.playCard("B", 3));
    }

    // to game rules you can play a card already played by another player if there isn't another option, this check it
    @Test
    void cantPlayOtherException() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {

        for (int i = 1; i<9; i++){
            testCardHandler.playCard("A", i);
            testCardHandler.prepareForNextRound();
        }
        testCardHandler.prepareForNextRound();
        testCardHandler.playCard("B", 10);
        testCardHandler.playCard("C", 9);

        testCardHandler.playCard("A", 10);
        assertNotNull(testCardHandler);
    }

    @Test
    void getOrderedPlayersTest() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        for (int i = 1; i<9; i++){
            testCardHandler.playCard("A", i);
            testCardHandler.prepareForNextRound();
        }
        testCardHandler.prepareForNextRound();
        testCardHandler.playCard("B", 10);
        testCardHandler.playCard("C", 9);
        testCardHandler.playCard("A", 10);
        assertEquals( List.of("C","B","A"), testCardHandler.getOrderedPlayers());
        assertEquals(testCardHandler.getOrderedPlayers().get(0), testCardHandler.getWinningPlayer());
        String PlayerZero = testCardHandler.getOrderedPlayers().get(0);
        testCardHandler.prepareForNextRound();
        assertEquals(PlayerZero, testCardHandler.getFirstPlayerToMove());
        this.setup();

        testCardHandler.playCard("B", 5);
        testCardHandler.playCard("C", 3);
        testCardHandler.playCard("A", 2);
        assertEquals( List.of("A","C","B"), testCardHandler.getOrderedPlayers());
        assertEquals(testCardHandler.getOrderedPlayers().get(0), testCardHandler.getWinningPlayer());
        PlayerZero = testCardHandler.getOrderedPlayers().get(0);
        testCardHandler.prepareForNextRound();
        assertEquals(PlayerZero, testCardHandler.getFirstPlayerToMove());
        this.setup();

        testCardHandler.playCard("B", 1);
        testCardHandler.playCard("C", 2);
        testCardHandler.playCard("A", 3);
        assertEquals( List.of("B","C","A"), testCardHandler.getOrderedPlayers());
        assertEquals(testCardHandler.getOrderedPlayers().get(0), testCardHandler.getWinningPlayer());
        PlayerZero = testCardHandler.getOrderedPlayers().get(0);
        testCardHandler.prepareForNextRound();
        assertEquals(PlayerZero, testCardHandler.getFirstPlayerToMove());
        this.setup();

        for (int i = 2; i < 11; i++){
            testCardHandler.playCard("B", i);
            testCardHandler.prepareForNextRound();
            testCardHandler.playCard("C", i);
            testCardHandler.prepareForNextRound();
        }
        testCardHandler.playCard("A", 1);
        testCardHandler.playCard("B", 1);
        testCardHandler.playCard("C", 1);

        assertEquals(List.of("A","B","C"), testCardHandler.getOrderedPlayers());
        assertEquals(testCardHandler.getOrderedPlayers().get(0), testCardHandler.getWinningPlayer());
        PlayerZero = testCardHandler.getOrderedPlayers().get(0);
        testCardHandler.prepareForNextRound();
        assertEquals(PlayerZero, testCardHandler.getFirstPlayerToMove());
        testCardHandler.hashCode();
    }



    //correct setup of next turn
    @Test
    void nextTurn() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {

        testCardHandler.playCard("A", 1);
        testCardHandler.playCard("B", 2);
        testCardHandler.prepareForNextRound();

        assertTrue(testCardHandler.playersOnTheTable.values().stream().map(PlayerCards::hasPlayedCard).allMatch(i->i==false));
    }

    @Test
    void mockFlawlessGame() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        for (int i=1;i<=5;i++){
            testCardHandler.playCard("A", i);
            testCardHandler.playCard("B", i+1);
            testCardHandler.playCard("C", i+2);
            testCardHandler.playCard("D", i+3);
            testCardHandler.prepareForNextRound();
        }
        assertNotNull(testCardHandler);
    }

    @Test
    void lastPlayedCard() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        testCardHandler.playCard("A", 1);
        assertEquals(testCardHandler.lastPlayedCard("A"), testCardHandler.playersOnTheTable.get("A").getLastCard());
    }
    @Test
    void lastPlayedCardMovement() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        testCardHandler.playCard("A", 1);
        assertEquals(Objects.requireNonNull(testCardHandler.lastPlayedCard("A")).getMovementValue(), Objects.requireNonNull(testCardHandler.playersOnTheTable.get("A").getLastCard()).getMovementValue());
    }

    @Test
    void correctWinner() throws IllegalApprenticeCardException, AlreadyPlayedCardException, OutOfBoundIntegerCardException {
        // normal use
        testCardHandler.playCard("A", 1);
        testCardHandler.playCard("B", 3);
        assertEquals("A", testCardHandler.getWinningPlayer());
        testCardHandler.prepareForNextRound();
        //assertEquals(null, testCardHandler.getWinningPlayer());

        //test cant play any other card exception
        for(int i=2;i<9;i++){
            testCardHandler.playCard("A", i);
            testCardHandler.prepareForNextRound();
        }
        testCardHandler.playCard("B", 9);
        testCardHandler.playCard("C", 10);
        testCardHandler.playCard("A", 9);
        System.out.println(testCardHandler.lastPlayedCard("A"));
        assertEquals("B", testCardHandler.getWinningPlayer());

    }

}