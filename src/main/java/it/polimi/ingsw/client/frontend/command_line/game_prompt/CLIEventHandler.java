package it.polimi.ingsw.client.frontend.command_line.game_prompt;

import it.polimi.ingsw.debug_utility.DebugLogger;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.game_event.GameEventHandler;
import it.polimi.ingsw.model.game_event.game_events.*;
import it.polimi.ingsw.model.game_event.game_events.character_cards.*;

import java.io.PrintWriter;
import java.util.logging.Level;

public class CLIEventHandler implements GameEventHandler {
    private final PrintWriter out;

    public CLIEventHandler(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void acceptEvent(GameEvent event) {
        out.print("event - ");
        GameEventHandler.super.acceptEvent(event);
    }

    @Override
    public void gameStarted(GameStartedEvent gameStartedEvent) {
        out.println("game just started!");
    }

    @Override
    public void commandFailed(CommandFailedEvent commandFailedEvent) {
        out.println("ERROR!: " + commandFailedEvent.getErrorMessage());
    }

    @Override
    public void bagToCloud(BagToCloudEvent bagToCloudEvent) {
        out.println(bagToCloudEvent.getTokens() + "tokens skyrocketed from bag to cloud " + bagToCloudEvent.getCloudIndex());
    }

    @Override
    public void gameIsOver(GameIsOverEvent gameIsOverEvent) {
        out.println("""
                      ::::::::      :::       :::   :::   ::::::::::          ::::::::  :::     ::: :::::::::: :::::::::\s
                    :+:    :+:   :+: :+:    :+:+: :+:+:  :+:                :+:    :+: :+:     :+: :+:        :+:    :+:\s
                   +:+         +:+   +:+  +:+ +:+:+ +:+ +:+                +:+    +:+ +:+     +:+ +:+        +:+    +:+ \s
                  :#:        +#++:++#++: +#+  +:+  +#+ +#++:++#           +#+    +:+ +#+     +:+ +#++:++#   +#++:++#:   \s
                 +#+   +#+# +#+     +#+ +#+       +#+ +#+                +#+    +#+  +#+   +#+  +#+        +#+    +#+   \s
                #+#    #+# #+#     #+# #+#       #+# #+#                #+#    #+#   #+#+#+#   #+#        #+#    #+#    \s
                ########  ###     ### ###       ### ##########          ########      ###     ########## ###    ###    \s
                """);
        out.println("game is over, winner is: " + gameIsOverEvent.getWinner());
    }

    @Override
    public void playedApprenticeCard(PlayedApprenticeCardEvent playedApprenticeCardEvent) {
        out.println("ApprenticeCard "+ playedApprenticeCardEvent.getCardID() +" played by: " + playedApprenticeCardEvent.getPlayerID()+", he loves Apprenticeship since its a cheap option");
    }

    @Override
    public void professorChanged(ProfessorChangedEvent professorChangedEvent) {
        out.println("Professor "+ professorChangedEvent.getToken() +" changed owner to: " + professorChangedEvent.getPlayerID()+"... remember that stealing is bad, unless we do it");
    }

    @Override
    public void tokenToStudyHall(TokenToStudyHallEvent tokenToStudyHallEvent) {
        out.println("token "+ tokenToStudyHallEvent.getToken() +" decided intentionally to go to " + tokenToStudyHallEvent.getPlayerID() + "entrance hall");
    }

    @Override
    public void tokenToIsland(TokenToIslandEvent tokenToIslandEvent) {
        out.println("token "+ tokenToIslandEvent.getToken() +" went on a trip to island: " + tokenToIslandEvent.getIslandIndex() + " by " + tokenToIslandEvent.getPlayerID());
    }

    @Override
    public void moveMotherNature(MoveMotherNatureEvent moveMotherNatureEvent) {
        out.println("Mother Nature moved from tropical island "+ moveMotherNatureEvent.getFrom() +" to island " + moveMotherNatureEvent.getTo() + "for a well deserved rest");
    }

    @Override
    public void towersToDashBoard(TowersToDashBoardEvent towersToDashBoardEvent) {
        out.println("Towers "+ towersToDashBoardEvent.getTower() +" back to" + towersToDashBoardEvent.getPlayerID() + " from "+ towersToDashBoardEvent.getIsland());
    }

    @Override
    public void towersToIsland(TowersToIslandEvent towersToIslandEvent) {
        out.println("Towers "+ towersToIslandEvent.getTower() +" were kidnapped from " + towersToIslandEvent.getPlayerID() + " to island "+ towersToIslandEvent.getIsland());
    }

    @Override
    public void mergedIslands(MergedIslandsEvent mergedIslandsEvent) {
        out.println("island#" + mergedIslandsEvent.getIslandIndex1() + " decided to eat island#" + mergedIslandsEvent.getIslandIndex2() + " and liked it! (all islands indexes were changed as a result)");
        DebugLogger.log("napoli, juve, aperol!", Level.INFO);
    }

    @Override
    public void cloudToEntranceHall(CloudToEntranceHallEvent cloudToEntranceHallEvent) {
        out.println("cloud#" + cloudToEntranceHallEvent.getCloudIndex() + "(whose content is:" + cloudToEntranceHallEvent.getContent() +") was legally stolen by "+ cloudToEntranceHallEvent.getReceivingPlayer());
    }

    @Override
    public void ccActivated(CCActivatedEvent ccActivatedEvent) {
        out.println("Character Card#" + ccActivatedEvent.getCardName() + " has been awakened in the middle of a nap.\n"+ccActivatedEvent.getDescription());
    }

    @Override
    public void ccDeActivated(CCDeActivated ccDeActivated) {
        out.println("Character Card#" + ccDeActivated.getCardName() + " finally has gone to sleep again.");
    }

    @Override
    public void bagToCard(BagTokenToCardEvent bagTokenToCardEvent) {
        out.println("Token " + bagTokenToCardEvent.getToken() + " jumped from bag to " + bagTokenToCardEvent.getCardName() + "i guess he is the chosen one");
    }

    @Override
    public void cardToEntranceHall(CardToEntranceHallEvent cardToEntranceHallEvent) {
        out.println("Tokens " + cardToEntranceHallEvent.getTokens() + " came back from vacation in card " + cardToEntranceHallEvent.getCardName() + " and suddenly went to their uncle " + cardToEntranceHallEvent.getPlayerID());
    }

    @Override
    public void cardTokenToIsland(CardTokenToIslandEvent cardTokenToIslandEvent) {
        out.println("Token " + cardTokenToIslandEvent.getToken() + " was annoyed by " + cardTokenToIslandEvent.getCardName() +  " and started a trip to island " + cardTokenToIslandEvent.getIslandIndex());
    }

    @Override
    public void cardTokenToStudyHall(CardTokenToStudyHallEvent cardTokenToStudyHallEvent) {
        out.println("Token " + cardTokenToStudyHallEvent.getToken() + " teleported from " + cardTokenToStudyHallEvent.getCardName() +  " to " + cardTokenToStudyHallEvent.getPlayerID()+" because he can");
    }

    @Override
    public void entranceHallToCard(EntranceHallToCardEvent entranceHallToCardEvent) {
        out.println("Tokens " + entranceHallToCardEvent.getTokens() + " disappeared from " + entranceHallToCardEvent.getPlayerID() + " and were found hardcore partying in " + entranceHallToCardEvent.getCardName());
    }

    @Override
    public void coinsChanged(CoinsChangedEvent coinsChangedEvent) {
        out.println(coinsChangedEvent.getPlayerId() + "'s coins are now: " + coinsChangedEvent.getCurrentValue());
    }

    @Override
    public void newState(NewStateGameEvent newStateGameEvent) {
        out.println("state changed to: " + newStateGameEvent.getNewStateName());
    }

    @Override
    public void currentPlayerChanged(CurrentPlayerChangedGameEvent currentPlayerChangedGameEvent) {
        out.println("current player is now: " + currentPlayerChangedGameEvent.getCurrentPlayer()+" now is your time to shine champ");
    }

}
