package it.polimi.ingsw.model.dashboard;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class DashBoardTest {

    @Test
    void moveToEntranceHallTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.RED, 1);
        dashBoardObject.moveToEntranceHall(coll);
        dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
        assertTrue(dashBoardObject.cloneEntranceHall().get(TokenEnum.GREEN) == 1 &&
                dashBoardObject.cloneEntranceHall().get(TokenEnum.RED) == 1 &&
                dashBoardObject.cloneEntranceHall().get(TokenEnum.BLUE) == 0 &&
                dashBoardObject.cloneEntranceHall().get(TokenEnum.YELLOW) == 0 &&
                dashBoardObject.cloneEntranceHall().get(TokenEnum.VIOLET) == 0);
    }

    @Test
    void moveToEntranceHallExcpetionTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.RED, 8);
        dashBoardObject.moveToEntranceHall(coll);
        dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
        assertThrows(ReloadGameException.class, () -> dashBoardObject.moveToEntranceHall(TokenEnum.RED));
    }

    @Test
    void moveToEntranceHallStackExceptionTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 10);
        assertThrows(ReloadGameException.class, () -> dashBoardObject.moveToEntranceHall(coll));
    }

    @Test
    void moveToStudyHallFromEntranceNegativeAmountExceptionTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.moveToStudyHallFromEntrance(TokenEnum.RED));
    }

    @Test
    void addCoinsNegativeInputExceptionTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        assertThrows(NegativeInputException.class, () -> dashBoardObject.addCoins(-1));
    }

    @Test
    void moveToStudyHallStackExcpetion() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        for(int i = 0; i < 10; i++)
            dashBoardObject.moveToStudyHall(TokenEnum.RED);
        assertThrows(ReloadGameException.class, () -> dashBoardObject.moveToStudyHall(TokenEnum.RED));
    }

    @Test
    void removeFromEntranceHallTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
        dashBoardObject.removeFromEntranceHall(TokenEnum.GREEN);
        assertEquals(0, dashBoardObject.cloneEntranceHall().get(TokenEnum.GREEN));
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.removeFromEntranceHall(TokenEnum.RED));
    }

    @Test
    void removeFromEntranceHallCollectionTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
        dashBoardObject.moveToEntranceHall(TokenEnum.RED);
        dashBoardObject.moveToEntranceHall(TokenEnum.YELLOW);
        dashBoardObject.moveToEntranceHall(TokenEnum.BLUE);
        dashBoardObject.moveToEntranceHall(TokenEnum.VIOLET);
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.VIOLET, 1);
        coll.addTokens(TokenEnum.RED, 1);
        coll.addTokens(TokenEnum.BLUE, 1);
        coll.addTokens(TokenEnum.YELLOW, 1);
        coll.addTokens(TokenEnum.GREEN, 1);
        dashBoardObject.removeFromEntranceHall(coll);
        assertEquals(0, dashBoardObject.cloneEntranceHall().size());
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.removeFromEntranceHall(coll));
    }

    @Test
    void moveToStudyHallFromEntranceTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        for(int i = 0; i < 8; i++){
            dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
            dashBoardObject.moveToStudyHallFromEntrance(TokenEnum.GREEN);
        }
        assertTrue(dashBoardObject.cloneEntranceHall().get(TokenEnum.GREEN) == 0 && dashBoardObject.cloneStudyHall().get(TokenEnum.GREEN) == 8);

        for(int i = 0; i < 2; i++){
            dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
            dashBoardObject.moveToStudyHallFromEntrance(TokenEnum.GREEN);
        }
        dashBoardObject.moveToEntranceHall(TokenEnum.GREEN);
        assertThrows(ReloadGameException.class, ()-> dashBoardObject.moveToStudyHallFromEntrance(TokenEnum.GREEN));
    }

    public void printTokens(TokenCollection tkC){

        System.out.println("\nBLUE: " + tkC.get(TokenEnum.BLUE));
        System.out.println("RED: " + tkC.get(TokenEnum.RED));
        System.out.println("YELLOW: " + tkC.get(TokenEnum.YELLOW));
        System.out.println("GREEN: " + tkC.get(TokenEnum.GREEN));
        System.out.println("VIOLET: " + tkC.get(TokenEnum.VIOLET));
    }

    @Test
    void addCoinsTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        int startingCoins = dashBoardObject.getCoins();
        dashBoardObject.addCoins(5);
        assertEquals(startingCoins + 5, dashBoardObject.getCoins());
        assertThrows(NegativeInputException.class, () -> dashBoardObject.removeCoins(-1));
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.removeCoins(startingCoins + 10));
        assertThrows(PositiveAmountException.class, () -> dashBoardObject.addCoins(startingCoins + 15));
    }

    @Test
    void euqalTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        DashBoard dashBoardObject1 = new DashBoard("players", "mag", "squad");
        assertNotEquals(dashBoardObject, dashBoardObject1);
    }

    @Test
    void removeCoinsTest(){
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        int startingCoins = dashBoardObject.getCoins();
        dashBoardObject.addCoins(5);
        dashBoardObject.removeCoins(4);
        assertEquals(dashBoardObject.getCoins(), startingCoins + 1);

    }

    @Test
    void moveToEntranceHallTestColl() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        TokenCollection tkC = TokenCollection.newEmptyCollection();
        tkC.addTokens(TokenEnum.GREEN, 2);
        dashBoardObject.moveToEntranceHall(tkC);
        assertEquals(2, dashBoardObject.cloneEntranceHall().get(TokenEnum.GREEN));
    }

    @Test
    void removeFromStudyHallTest() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        dashBoardObject.moveToStudyHall(TokenEnum.RED);
        dashBoardObject.moveToStudyHall(TokenEnum.YELLOW);
        dashBoardObject.moveToStudyHall(TokenEnum.VIOLET);
        dashBoardObject.moveToStudyHall(TokenEnum.BLUE);
        dashBoardObject.moveToStudyHall(TokenEnum.GREEN);

        assertEquals("player", dashBoardObject.getPlayerID());

        dashBoardObject.removeFromStudyHall(TokenEnum.RED);
        dashBoardObject.removeFromStudyHall(TokenEnum.BLUE);
        dashBoardObject.removeFromStudyHall(TokenEnum.YELLOW);
        dashBoardObject.removeFromStudyHall(TokenEnum.GREEN);
        dashBoardObject.removeFromStudyHall(TokenEnum.VIOLET);

        assertEquals(0, dashBoardObject.cloneStudyHall().size());
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.removeFromStudyHall(TokenEnum.VIOLET));
    }

    @Test
    void removeFromStudyHallTest1() throws ReloadGameException {
        DashBoard dashBoardObject = new DashBoard("player", "mag", "squad");
        TokenCollection temp = TokenCollection.newEmptyCollection();
        temp.addTokens(TokenEnum.GREEN, 1);
        temp.addTokens(TokenEnum.VIOLET, 1);
        temp.addTokens(TokenEnum.YELLOW, 1);
        temp.addTokens(TokenEnum.BLUE, 1);
        temp.addTokens(TokenEnum.RED, 1);

        dashBoardObject.moveToStudyHall(TokenEnum.RED);
        dashBoardObject.moveToStudyHall(TokenEnum.YELLOW);
        dashBoardObject.moveToStudyHall(TokenEnum.VIOLET);
        dashBoardObject.moveToStudyHall(TokenEnum.BLUE);
        dashBoardObject.moveToStudyHall(TokenEnum.GREEN);

        assertEquals("player", dashBoardObject.getPlayerID());

        dashBoardObject.removeFromStudyHall(temp);

        assertEquals(0, dashBoardObject.cloneStudyHall().size());
        assertThrows(NegativeAmountException.class, () -> dashBoardObject.removeFromStudyHall(temp));
    }
}