package it.polimi.ingsw.model.character_cards;

import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.custom_json_builder.Gsonable;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.character_cards.CCDeActivated;

import java.util.logging.Level;


public abstract class CharacterCard extends GsonablePrototype implements Simplifiable {

    protected boolean currentlyActive = false;

    /**
     * Plays the Card as per the Visitor Pattern
     * @param board The board on which the card is going to be played
     * @param playerID The player which created the command
     * @param toActivate The token to be activated based on the CC
     * @param islandIndex The island index based on the CC
     * @param tokensToAdd The tokens to be added as in the Card 7
     * @param tokensToRemove The tokens to be removed in the Card 7
     * @throws ReloadGameException Thrown when there is an internal error
     */
    public abstract void playCard(Board board, String playerID, TokenEnum toActivate, int islandIndex, TokenCollection tokensToAdd, TokenCollection tokensToRemove) throws ReloadGameException;

    /**
     * Checks whether the CC is active
     * @throws ReloadGameException Thrown when the card is active
     */
    public void check() throws ReloadGameException {
        if(currentlyActive)
            throw new ReloadGameException(this.getClass().getSimpleName() + " has already been played.");
    }

    public String getCardName(){
        return this.getClass().getSimpleName();
    }

    public void reset(Board board){
        if(currentlyActive)
            board.notifyObservers(new CCDeActivated(this.getCardName()));

        this.currentlyActive = false;
    }


}
