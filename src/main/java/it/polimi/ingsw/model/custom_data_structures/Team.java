package it.polimi.ingsw.model.custom_data_structures;

import it.polimi.ingsw.controller.data_transfer_objects.DashboardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.custom_data_structures.exceptions.EmptyException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.islands.TowerEnum;
import it.polimi.ingsw.model.islands.exceptions.NoneTowerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team implements Simplifiable {
    private final String name;
    private final List<String> players;
    private final TowerEnum towerColour;
    private final IntegerStack numberOfTowers;

    public Team(String name, List<String> players, TowerEnum towerColour, int numberOfTowers) throws NegativeInputException, NoneTowerException {
        this.name = name;
        if (players.isEmpty()) throw new EmptyException();
        if (towerColour.equals(TowerEnum.NONE)) throw new NoneTowerException();

        this.players = List.copyOf(players);
        this.towerColour = towerColour;
        this.numberOfTowers = new IntegerStack(numberOfTowers);
    }

    /**
     * returns team's name
     *
     * @return team's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * returns a list with the player names
     *
     * @return a list with the player names
     */
    public List<String> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * returns the island's tower colour
     *
     * @return the island's tower colour
     */
    public TowerEnum getTowerColour() {
        return towerColour;
    }

    /**
     * returns the number of towers the team has left
     *
     * @return the number of remaining towers
     */
    public int getNumberOfTowers() {
        return numberOfTowers.size();
    }

    /**
     * gives the team the requested number of towers
     *
     * @param amountToAdd requested number of towers
     */
    public void addTowers(int amountToAdd) {
        this.numberOfTowers.add(amountToAdd);
    }

    /**
     * takes the requested amount of towers from the team
     *
     * @param amountToTake amount requested
     * @throws NegativeAmountException thrown when the number would fall below 0
     */
    //TODO: test that the amount actually decreases despite the exception
    public void takeTowers(int amountToTake) throws NegativeAmountException {
        for (int i = 0; i < amountToTake; i++) {
            this.numberOfTowers.decreaseBy(1);
        }

    }

    public Team copy() {
        return new Team(name, players, towerColour, numberOfTowers.size());
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", towerColour=" + towerColour +
                ", numberOfTowers=" + numberOfTowers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && Objects.equals(players, team.players) && towerColour == team.towerColour && Objects.equals(numberOfTowers, team.numberOfTowers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, players, towerColour, numberOfTowers);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        players.forEach(player -> addInfoToDashBoard(gameDTO, player));
    }

    private void addInfoToDashBoard(GameDTO gameDTO, String player) {
        DashboardDTO playerDB = gameDTO.getDashboards().get(player);
        playerDB.setTowerColour(towerColour);
        playerDB.setTowers(numberOfTowers.size());
    }
}
