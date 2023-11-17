package it.polimi.ingsw.model.game_event;

import it.polimi.ingsw.model.game_event.game_events.*;
import it.polimi.ingsw.model.game_event.game_events.character_cards.*;

public interface GameEventHandler extends GameEventReceiver {
    @Override
    default void acceptEvent(GameEvent event) {
        event.visit(this);
    }

    void gameStarted(GameStartedEvent gameStartedEvent);

    void commandFailed(CommandFailedEvent commandFailedEvent);

    void newState(NewStateGameEvent newStateGameEvent);

    void currentPlayerChanged(CurrentPlayerChangedGameEvent currentPlayerChangedGameEvent);

    void bagToCloud(BagToCloudEvent bagToCloudEvent);

    void gameIsOver(GameIsOverEvent gameIsOverEvent);

    void playedApprenticeCard(PlayedApprenticeCardEvent playedApprenticeCardEvent);

    void professorChanged(ProfessorChangedEvent professorChangedEvent);

    void tokenToStudyHall(TokenToStudyHallEvent tokenToStudyHallEvent);

    void tokenToIsland(TokenToIslandEvent tokenToIslandEvent);

    void moveMotherNature(MoveMotherNatureEvent moveMotherNatureEvent);

    void towersToDashBoard(TowersToDashBoardEvent towersToDashBoardEvent);

    void towersToIsland(TowersToIslandEvent towersToIslandEvent);

    void mergedIslands(MergedIslandsEvent mergedIslandsEvent);

    void cloudToEntranceHall(CloudToEntranceHallEvent cloudToEntranceHallEvent);

    void ccActivated(CCActivatedEvent ccActivatedEvent);

    void ccDeActivated(CCDeActivated ccDeActivated);

    void bagToCard(BagTokenToCardEvent bagTokenToCardEvent);

    void cardToEntranceHall(CardToEntranceHallEvent cardToEntranceHallEvent);

    void cardTokenToIsland(CardTokenToIslandEvent cardTokenToIslandEvent);

    void cardTokenToStudyHall(CardTokenToStudyHallEvent cardTokenToStudyHallEvent);

    void entranceHallToCard(EntranceHallToCardEvent entranceHallToCardEvent);

    void coinsChanged(CoinsChangedEvent coinsChangedEvent);
    // TODO : add subclasses methods!
}
