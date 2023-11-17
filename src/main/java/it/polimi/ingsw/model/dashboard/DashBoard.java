package it.polimi.ingsw.model.dashboard;


import it.polimi.ingsw.controller.data_transfer_objects.DashboardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.ModelEventCreator;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.PositiveAmountException;
import it.polimi.ingsw.model.game_event.game_events.CoinsChangedEvent;
import it.polimi.ingsw.model.game_event.game_events.TokenToStudyHallEvent;

import java.util.Objects;


public class DashBoard extends ModelEventCreator implements Simplifiable {

    private static final int MAX_AMOUNT_OF_STUDENTS_E = 9;
    private static final int MAX_AMOUNT_OF_STUDENTS_S = 10;
    private static final int MAX_AMOUNT_OF_COINS = 16;
    private final String playerID;
    private final String magician; //For the view, the actual logic is handled in the class CardHandler
    private final String squadName;

    private final TokenCollection entranceHall;
    private final TokenCollection studyHall;

    private int coins;

    public DashBoard(String playerID, String magician, String squadName) {
        this.playerID = playerID;
        this.magician = magician;
        this.squadName = squadName;
        entranceHall = TokenCollection.newEmptyCollection();
        studyHall = TokenCollection.newEmptyCollection();
        coins = 1;
    }

    /*
     * Returns a copy of the Study Hall
     * @return Copy of Study Hall
     */
    public TokenCollection cloneStudyHall() {
        return studyHall.copy();
    }

    /**
     * Returns a copy of the entrance hall
     *
     * @return Copy of entrance hall
     */
    public TokenCollection cloneEntranceHall() {
        return entranceHall.copy();
    }

    /**
     * Returns the owner's ID
     *
     * @return The owner's ID
     */
    public String getPlayerID() {
        return String.valueOf(this.playerID);
    }

    /**
     * Returns the amount of coins the dashboard has
     *
     * @return The amount of coins contained in the dashboard
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Adds a number of coins to the dashboard
     *
     * @param coins The amount of coins to be added
     */
    public void addCoins(int coins) throws NegativeInputException, PositiveAmountException {
        if (coins < 0)
            throw new NegativeInputException("You cannot add a negative amount of coins.");
        if (coins + this.coins > MAX_AMOUNT_OF_COINS)
            throw new PositiveAmountException("You tried to add more coins that the allowed amount.");
        this.coins += coins;
        notifyObservers(new CoinsChangedEvent(this.playerID, this.coins));
    }

    /**
     * Adds coins to the dashboard
     *
     * @param coins The amount of coins to be added
     * @throws NegativeInputException  Thrown when the amount of coins is negative
     * @throws NegativeAmountException Thrown when the amount of coins to be removed is greater than the amount available
     */
    public void removeCoins(int coins) throws NegativeInputException, NegativeAmountException {
        if (coins < 0)
            throw new NegativeInputException("The amount od coins must be positive.");
        if (this.coins < coins)
            throw new NegativeAmountException("You do not have enough coins to play the card. Your coins: " + Integer.toString(this.coins));
        this.coins -= coins;
        notifyObservers(new CoinsChangedEvent(this.playerID, this.coins));
    }

    /**
     * Moves a token from the Entrance hall to the Study hall
     *
     * @param tokenToMove the token to move from the entrance hall to the study hall
     */
    public void moveToStudyHallFromEntrance(TokenEnum tokenToMove) throws NegativeAmountException, ReloadGameException {
        try {
            entranceHall.popToken(tokenToMove, 1);
        } catch (Exception e) {
            throw new NegativeAmountException("The required amount of tokens are not in the entrance hall.");
        }
        if (studyHall.get(tokenToMove) + 1 > MAX_AMOUNT_OF_STUDENTS_S)
            throw new ReloadGameException("The amount of students of the specific type in the study hall is greater than the allowed capacity. The token cannot be moved from the entrance to the study hall");

        if (((studyHall.get(tokenToMove) + 1) % 3) == 0)
            this.addCoins(1);

        studyHall.addTokens(tokenToMove, 1);
        notifyObservers(new TokenToStudyHallEvent(playerID, tokenToMove));
    }

    /**
     * Moves to the study hall a token
     *
     * @param tokenToMove The token to be moved
     * @throws StackOverflowError Thrown when the amount of tokens in the study hall if greater than the limit
     */
    public void moveToStudyHall(TokenEnum tokenToMove) throws ReloadGameException {
        if (studyHall.get(tokenToMove) + 1 > MAX_AMOUNT_OF_STUDENTS_S)
            throw new ReloadGameException("The amount of students in the study hall is greater than the allowed capacity. The token cannot be added");

        if (((studyHall.get(tokenToMove) + 1) % 3) == 0)
            this.addCoins(1);

        studyHall.addTokens(tokenToMove, 1);
    }

    /**
     * Moves a token to the Entrance Hall
     *
     * @param tokenToMove The token to be moved to the Entrance Hall
     * @throws StackOverflowError Thrown when the amount of tokens in the entrance hall if greater than the limit
     */
    public void moveToEntranceHall(TokenEnum tokenToMove) throws ReloadGameException {
        if (entranceHall.size() + 1 > MAX_AMOUNT_OF_STUDENTS_E)
            throw new ReloadGameException("The amount of students in the entrance hall is greater than the allowed capacity. THe Token cannot be added");
        try {
            entranceHall.addTokens(tokenToMove, 1);
        } catch (Exception e) {
            throw new NegativeInputException("You cannot add a negative amount of tokens");
        }
    }

    /**
     * Adds a Collection of Tokens to the entrance of the dashboard
     *
     * @param tokensToMove The tokens to be added to the entrance hall
     * @throws StackOverflowError Thrown when the amount of tokens in the entrance hall if greater than the limit
     */
    public void moveToEntranceHall(TokenCollection tokensToMove) throws ReloadGameException {
        if (entranceHall.size() + tokensToMove.size() > MAX_AMOUNT_OF_STUDENTS_E)
            throw new ReloadGameException("The amount of students in the entrance hall is greater than the allowed capacity.The collection cannot be added");
        entranceHall.addToCollection(tokensToMove);
    }

    /**
     * Removes a token from the Entrance Hall
     *
     * @param tokenToRemove The token to be removed from the Entrance Hall
     */
    public void removeFromEntranceHall(TokenEnum tokenToRemove) throws NegativeAmountException {
        try {
            entranceHall.popToken(tokenToRemove, 1);
        } catch (Exception e) {
            throw new NegativeAmountException("you can't move tokens you don't have");
        }
    }

    /**
     * Removes a tokenCollection from the Study Hall
     *
     * @param tokensToRemove The tokens to be removed from the Study Hall
     */
    public void removeFromStudyHall(TokenCollection tokensToRemove) throws NegativeAmountException {
        try {
            for (int i = 0; i < tokensToRemove.get(TokenEnum.RED); i++)
                studyHall.popToken(TokenEnum.RED, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.YELLOW); i++)
                studyHall.popToken(TokenEnum.YELLOW, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.GREEN); i++)
                studyHall.popToken(TokenEnum.GREEN, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.BLUE); i++)
                studyHall.popToken(TokenEnum.BLUE, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.VIOLET); i++)
                studyHall.popToken(TokenEnum.VIOLET, 1);
        } catch (Exception e) {
            throw new NegativeAmountException("The StudyHall does not contain the required amount of tokens required from the collection.");
        }
    }

    /**
     * Removes a token from the Study Hall
     *
     * @param tokenToRemove The token to be removed from the Study Hall
     */
    public void removeFromStudyHall(TokenEnum tokenToRemove) throws NegativeAmountException {
        try {
            studyHall.popToken(tokenToRemove, 1);
        } catch (Exception e) {
            throw new NegativeAmountException();
        }
    }

    /**
     * Removes a tokenCollection from the Entrance Hall
     *
     * @param tokensToRemove The tokens to be removed from the Entrance Hall
     */
    public void removeFromEntranceHall(TokenCollection tokensToRemove) throws NegativeAmountException {
        try {
            for (int i = 0; i < tokensToRemove.get(TokenEnum.RED); i++)
                entranceHall.popToken(TokenEnum.RED, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.YELLOW); i++)
                entranceHall.popToken(TokenEnum.YELLOW, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.GREEN); i++)
                entranceHall.popToken(TokenEnum.GREEN, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.BLUE); i++)
                entranceHall.popToken(TokenEnum.BLUE, 1);

            for (int i = 0; i < tokensToRemove.get(TokenEnum.VIOLET); i++)
                entranceHall.popToken(TokenEnum.VIOLET, 1);
        } catch (Exception e) {
            throw new NegativeAmountException("Is was not possible to remove the tokens in the collection from the Entrance Hall");
        }
    }

    /**
     * Checks if the input object is equal to the current object
     *
     * @param o The object ot be checked
     * @return True - The object is the same; False - The object is different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashBoard dashBoard = (DashBoard) o;
        return coins == dashBoard.coins && playerID.equals(dashBoard.playerID) && magician.equals(dashBoard.magician) && squadName.equals(dashBoard.squadName) && entranceHall.equals(dashBoard.entranceHall) && studyHall.equals(dashBoard.studyHall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MAX_AMOUNT_OF_STUDENTS_E, MAX_AMOUNT_OF_STUDENTS_S, playerID, magician, squadName, entranceHall, studyHall, coins);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        DashboardDTO simpleDashBoard = gameDTO.getDashboards().get(playerID);
        simpleDashBoard.setOwner(playerID);
        simpleDashBoard.setEntranceHall(entranceHall.getMap());
        simpleDashBoard.setStudyHall(studyHall.getMap());
        simpleDashBoard.setCoins(coins);
    }
}