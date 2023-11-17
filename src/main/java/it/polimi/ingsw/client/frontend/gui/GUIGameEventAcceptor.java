package it.polimi.ingsw.client.frontend.gui;

import it.polimi.ingsw.client.frontend.gui.screen_controllers.MainGameScreenController;
import it.polimi.ingsw.model.game_event.GameEventHandler;
import it.polimi.ingsw.model.game_event.game_events.*;
import it.polimi.ingsw.model.game_event.game_events.character_cards.*;
import javafx.application.Platform;


public class GUIGameEventAcceptor implements GameEventHandler {
    MainGameScreenController controller;

    @Override
    public void gameStarted(GameStartedEvent gameStartedEvent) {
    }

    @Override
    public void commandFailed(CommandFailedEvent commandFailedEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.commandFailed(commandFailedEvent);
            });
        }
    }

    @Override
    public void newState(NewStateGameEvent newStateGameEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.newState(newStateGameEvent);
            });
        }
    }

    @Override
    public void currentPlayerChanged(CurrentPlayerChangedGameEvent currentPlayerChangedGameEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.currentPlayerChanged(currentPlayerChangedGameEvent);
            });
        }
    }

    @Override
    public void bagToCloud(BagToCloudEvent bagToCloudEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.bagToCloud(bagToCloudEvent);
            });
        }
    }

    @Override
    public void gameIsOver(GameIsOverEvent gameIsOverEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.gameIsOver(gameIsOverEvent);
            });
        }
    }

    @Override
    public void playedApprenticeCard(PlayedApprenticeCardEvent playedApprenticeCardEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.playedApprenticeCard(playedApprenticeCardEvent);
            });
        }
    }

    @Override
    public void professorChanged(ProfessorChangedEvent professorChangedEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.professorChanged(professorChangedEvent);
            });
        }
    }

    @Override
    public void tokenToStudyHall(TokenToStudyHallEvent tokenToStudyHallEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.tokenToStudyHall(tokenToStudyHallEvent);
            });
        }
    }

    @Override
    public void tokenToIsland(TokenToIslandEvent tokenToIslandEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.tokenToIsland(tokenToIslandEvent);
            });
        }
    }

    @Override
    public void moveMotherNature(MoveMotherNatureEvent moveMotherNatureEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.moveMotherNature(moveMotherNatureEvent);
            });
        }
    }

    @Override
    public void towersToDashBoard(TowersToDashBoardEvent towersToDashBoardEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.towersToDashBoard(towersToDashBoardEvent);
            });
        }
    }

    @Override
    public void towersToIsland(TowersToIslandEvent towersToIslandEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.towersToIsland(towersToIslandEvent);
            });
        }
    }

    @Override
    public void mergedIslands(MergedIslandsEvent mergedIslandsEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.mergedIslands(mergedIslandsEvent);
            });
        }
    }

    @Override
    public void cloudToEntranceHall(CloudToEntranceHallEvent cloudToEntranceHallEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.cloudToEntranceHall(cloudToEntranceHallEvent);
            });
        }
    }

    @Override
    public void ccActivated(CCActivatedEvent ccActivatedEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.ccActivated(ccActivatedEvent);
            });
        }
    }

    @Override
    public void ccDeActivated(CCDeActivated ccDeActivated) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.ccDeActivated(ccDeActivated);
            });
        }
    }

    @Override
    public void bagToCard(BagTokenToCardEvent bagTokenToCardEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.bagToCard(bagTokenToCardEvent);
            });
        }
    }

    @Override
    public void cardToEntranceHall(CardToEntranceHallEvent cardToEntranceHallEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.cardToEntranceHall(cardToEntranceHallEvent);
            });
        }
    }

    @Override
    public void entranceHallToCard(EntranceHallToCardEvent entranceHallToCardEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.entranceHallToCard(entranceHallToCardEvent);
            });
        }
    }

    @Override
    public void coinsChanged(CoinsChangedEvent coinsChangedEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.coinsChanged(coinsChangedEvent);
            });
        }
    }

    @Override
    public void cardTokenToIsland(CardTokenToIslandEvent cardTokenToIslandEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.cardTokenToIsland(cardTokenToIslandEvent);
            });
        }
    }

    @Override
    public void cardTokenToStudyHall(CardTokenToStudyHallEvent cardTokenToStudyHallEvent) {
        if(Properties.getInstance().getCurrentScreen().equals("MainGameScreen")){
            Platform.runLater(()->{
                controller = Properties.getInstance().getFxmlLoader().getController();
                controller.cardTokenToStudyHall(cardTokenToStudyHallEvent);
            });
        }
    }
}
