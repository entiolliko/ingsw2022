package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.InfluenceHandler;
import it.polimi.ingsw.model.Professors;
import it.polimi.ingsw.model.islands.IslandChain;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MoveMotherNature extends GsonablePrototype implements PlayerVisitorCommand {
    private final String playerID;
    private final int movement;

    public MoveMotherNature(String player, int movement) {
        super();
        this.playerID = player;
        this.movement = movement;
    }

    @Override
    public String getPlayerID() {
        return this.playerID;
    }

    @Override
    public void visit(Board board) throws ReloadGameException {
        int maximumMovement = getLastPlayerCardMovement(board, playerID);
        int minimumMovement = 1;
        moveMotherNature(board, Math.max(minimumMovement, Math.min(movement, maximumMovement)));
        String winner = findPlayerWithMostInfluence(board);
        changeTowerColour(board, winner);
        mergeIslands(board);
    }

    private int getLastPlayerCardMovement(@NotNull Board board, String player) {
        return board
                .getCardHandler()
                .lastPlayedCardMovement(player);
    }

    private void moveMotherNature(@NotNull Board board, int amount) {
        IslandChain islands = board.getIslands();
        islands.moveMotherNature(amount);
    }

    private String findPlayerWithMostInfluence(@NotNull Board board) {
        InfluenceHandler influenceHandler = board.getInfluenceHandler();
        IslandChain islands = board.getIslands();
        Professors professors = board.getProfessors();

        return influenceHandler.winner(islands, professors);
    }

    private void changeTowerColour(@NotNull Board board, String winner) {
        IslandChain islands = board.getIslands();
        islands.changeCurrentIslandTowersWithThoseOfTheWinner(winner);
    }

    private void mergeIslands(@NotNull Board board) {
        board
                .getIslands()
                .mergeIslands();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveMotherNature that = (MoveMotherNature) o;
        return movement == that.movement && Objects.equals(playerID, that.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, movement);
    }
}
