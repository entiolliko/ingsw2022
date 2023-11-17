package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.custom_data_structures.IntegerStack;
import it.polimi.ingsw.model.custom_data_structures.Team;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.game_event.GameEventReceiver;
import it.polimi.ingsw.model.game_event.game_events.CurrentPlayerChangedGameEvent;
import it.polimi.ingsw.model.game_event.game_events.NewStateGameEvent;
import it.polimi.ingsw.model.interfaces.Visitable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


public class Game extends ModelEventCreator implements Visitable {
    private String gameID;
    private final Board board;
    private final List<String> players;
    private final TypeOfGame typeOfGame;
    private String currentState;
    private String currentPlayer;
    private boolean endAtEndOfTurn;

    public Game(@NotNull List<String> playersID, @NotNull List<String> magicians, @NotNull List<String> squadNames, @NotNull String gameID, TypeOfGame typeOfGame) {
        this.players = new ArrayList<>(playersID);
        this.board = new Board(playersID, magicians, squadNames, typeOfGame.equals(TypeOfGame.EXPERT));
        this.gameID = gameID;
        this.typeOfGame = typeOfGame;

        endAtEndOfTurn = false;

        this.currentState = "PlayApprenticeCardState";
        this.currentPlayer = getPlayersClockwise().get(0);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
        notifyObservers(new NewStateGameEvent(currentState));
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyObservers(new CurrentPlayerChangedGameEvent(currentPlayer));
    }

    /**
     * Accept and run the VisitorCommand on self
     *
     * @param visitor the VisitorCommand obj
     */
    @Override
    public void accept(@NotNull VisitorCommand visitor) throws ReloadGameException {
        //The commands are filtered on the state class level
        visitor.visit(this);
        if (this.board.getBag().isEmpty() || this.board.getCardHandler().gameOverCondition())
            endAtEndOfTurn = true;
    }

    /**
     * Returns the ID of the game
     *
     * @return The ID of the game
     */
    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * Returns the game's board
     *
     * @return The board
     */
    public Board getGameBoard() {
        return this.board;
    }

    /**
     * Returns the list of the players ordered based on the assistant cards player
     *
     * @return List of the ordered players
     */
    public List<String> getOrderedPlayers() {
        return board.getCardHandler().getOrderedPlayers();
    }

    /**
     * Returns the list of players ordered based on the turn order
     *
     * @return List of the ordered players
     */
    public List<String> getPlayersClockwise() {
        //a b c d --> c d a b
        String firstToPlay = board.getCardHandler().getFirstPlayerToMove();

        int pivot = this.players.indexOf(firstToPlay);
        List<String> returnValue = new ArrayList<>(players.subList(pivot, players.size()));
        returnValue.addAll(players.subList(0, pivot));
        return returnValue;
    }


    /**
     * Return the type of game
     *
     * @return The type of the game
     */
    public TypeOfGame getTypeOfGame() {
        return this.typeOfGame;
    }

    /**
     * Specifies to the state machine that the game is going to be over at the end of this round
     */
    public void setIsGoingToBeOver() {
        this.endAtEndOfTurn = true;
    }

    /**
     * Checks weather the game is going to be over at the end of this round
     *
     * @return The result of the calculation
     */
    public boolean isGoingToBeOver() {
        return endAtEndOfTurn;
    }

    /**
     * Checks weather one of the conditions of instant game over is true
     *
     * @return Returns the result of the calculation
     */
    public boolean instantGameOverCondition() {
        return
                this.board.getIslands().gameOverCondition();
    }

    /**
     * Returns the ID of the player who is leading the game
     *
     * @return
     */
    public List<String> getLeads() {
        Map<String, IntegerStack> scores = new HashMap<>();
        System.out.println("game.getLead");
        assignPlayerTowers(scores);
        System.out.println("scores after towers : " + scores);
        scores = filterHighestScores(scores);
        System.out.println("current maximums : " + scores);
        addOwnedProfessorsToScore(scores);
        System.out.println("current bitches(after profs) : " + scores);
        scores = filterHighestScores(scores);
        System.out.println("final result + :" + scores);

        return scores
                .keySet()
                .stream()
                .toList()
                ;
    }

    private void assignPlayerTowers(Map<String, IntegerStack> scores) {
        //gets number of towers owned by each player
        int startingTowers = this.getGameBoard().getIslands().numberOfTowersForThisGame();
        for (String player : board.getPlayersID()) {
            Optional<Team> playerTeam = board.getIslands().getTeamOf(player);
            playerTeam.ifPresent(team -> scores.put(player, new IntegerStack(startingTowers - team.getNumberOfTowers())));
        }
    }

    private int getHighestScore(Map<String, IntegerStack> scores) {
        //leaves only hte players with the biggest number of towers
        return scores
                .values()
                .stream()
                .mapToInt(IntegerStack::size)
                .max()
                .orElse(0)
                ;
    }

    private Map<String, IntegerStack> filterHighestScores(Map<String, IntegerStack> scores) {
        int maximumScore = getHighestScore(scores);
        return scores
                .entrySet()
                .stream()
                .filter(player -> player.getValue().size() >= maximumScore)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ;
    }

    private void addOwnedProfessorsToScore(Map<String, IntegerStack> scores) {
        for (String player : scores.keySet()) {
            int ownedProfs = board
                    .getProfessors()
                    .getOwners()
                    .values()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(x -> x.equals(player))
                    .toList().size();
            scores.get(player).add(ownedProfs);
        }
    }

    @Override
    public String getClassName() {
        return this.getClass().getName();
    }

    @Override
    public String toString() {
        return "Game{" + "\n" +
                "gameID='" + gameID + '\'' + "\n" +
                ", board=" + board + "\n" +
                ", playersOrder=" + players + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return endAtEndOfTurn == game.endAtEndOfTurn && Objects.equals(gameID, game.gameID) && Objects.equals(board, game.board) && Objects.equals(players, game.players) && typeOfGame == game.typeOfGame;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, board, players, typeOfGame, endAtEndOfTurn);
    }

    public GameDTO getDTO() {
        GameDTO returnValue = new GameDTO(players);
        returnValue.setGamePhase(currentState);
        returnValue.setCurrentPlayer(currentPlayer);
        board.fillDTO(returnValue);
        return returnValue;
    }

    @Override
    public void addEventObserver(GameEventReceiver eventObserver) {
        super.addEventObserver(eventObserver);
        this.board.addEventObserver(eventObserver);
    }

    @Override
    public Set<GameEventReceiver> popReceivers() {
        this.board.popReceivers();
        return super.popReceivers();
    }
}