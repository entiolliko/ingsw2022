package it.polimi.ingsw.model.visitor.base_commands;

import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.game_event.game_events.BagToCloudEvent;

public class FillClouds implements VisitorCommand {

    @Override
    public void visit(Board board) {
        int numOfPlayers = board.getPlayersID().size();
        TokenCollection tokensToBeAdded;

        for (int i = 0; i < board.getClouds().size(); i++) {
            int tokensToRemove = Math.min(board.getBag().size(), numOfPlayers == 3 ? 4 : 3);
            tokensToBeAdded = board.getBag().randomPop(tokensToRemove);

            board.getClouds().get(i).addTokens(tokensToBeAdded);
            //board.triggerGE(new FilledCloud(i, tokensToBeAdded))
            board.notifyObservers(new BagToCloudEvent(i, tokensToBeAdded.getMap()));
        }
    }
}
