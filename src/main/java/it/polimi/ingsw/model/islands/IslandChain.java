package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.IslandDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.ModelEventCreator;
import it.polimi.ingsw.model.custom_data_structures.CircularList;
import it.polimi.ingsw.model.custom_data_structures.Team;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.SameNameException;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.MergedIslandsEvent;
import it.polimi.ingsw.model.game_event.game_events.MoveMotherNatureEvent;
import it.polimi.ingsw.model.game_event.game_events.TowersToDashBoardEvent;
import it.polimi.ingsw.model.game_event.game_events.TowersToIslandEvent;
import it.polimi.ingsw.model.islands.exceptions.GameShouldBeOverException;
import it.polimi.ingsw.model.islands.exceptions.MissingMotherNatureException;
import it.polimi.ingsw.model.islands.exceptions.NoneTowerException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>IslandChain manages the islands and the various teams' towers</p>
 * <p>its most important methods are:</p>
 * <p>moveMotherNature</p>
 * <p>addTempMotherNature</p>
 * <p>addTokens</p>
 * <p>changeIslandTokens</p>
 * <p>mergeIslands</p>
 */
public class IslandChain extends ModelEventCreator implements Simplifiable {
    protected static final Map<Integer, Integer> startingNumberOfTowersPerPlayer = Map.of(2, 8, 3, 6, 4, 8);
    private static final int STARTING_NUMBER_OF_ISLANDS = 12;

    private final Map<String, Team> teams;
    private final CircularList<Island> islands;

    protected IslandChain() {
        islands = new CircularList<>();
        teams = new HashMap<>();
    }

    /**
     * creates the default islands chain for playing the game
     *
     * @param teams a map {teamName: teamPlayers}
     * @return an IslandChain with the desired teams
     * @throws IllegalArgumentException thrown when there's a different number of players then 2, 3 of 4
     * @throws SameNameException        thrown when there are homonym players
     */
    public static IslandChain newDefaultIslandChain(Map<String, List<String>> teams) throws IllegalArgumentException, SameNameException {
        if (teamsHaveInvalidNumbersOfPlayers(teams))
            throw new IllegalArgumentException("Illegal number of players in team");
        if (teamsHaveHomonymPlayers(teams)) throw new SameNameException();

        IslandChain returnValue = new IslandChain();
        returnValue.generateStartingIslands();
        returnValue.generateTeams(teams);
        returnValue.placeMotherNatureOnFirstIsland();

        return returnValue;
    }

    private static boolean teamsHaveInvalidNumbersOfPlayers(Map<String, List<String>> teams) {
        return teams.values().stream().anyMatch(team -> team.isEmpty() || team.size() > 2);
    }

    private static boolean teamsHaveHomonymPlayers(Map<String, List<String>> teams) {
        return teams.values().stream()
                .mapToLong(Collection::size).sum() !=
                teams.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()).size();
    }

    private void generateStartingIslands() {
        for (int i = 0; i < STARTING_NUMBER_OF_ISLANDS; i++)
            this.islands.add(Island.newEmptyIsland());

    }

    private void generateTeams(Map<String, List<String>> teams) {
        Iterator<TowerEnum> towerColours = Arrays.stream(TowerEnum.values()).toList().listIterator();
        for (String name : teams.keySet().stream().sorted().toList()) {
            this.teams.put(name,
                    new Team(name, teams.get(name), towerColours.next(), numberOfTowersForThisGame(teams)));
        }
    }

    public Integer numberOfTowersForThisGame(Map<String, List<String>> teams) {
        return startingNumberOfTowersPerPlayer.get(teams.keySet().size());
    }

    public Integer numberOfTowersForThisGame() {
        return startingNumberOfTowersPerPlayer.get(this.teams.keySet().size());
    }

    //to use only during setup
    private void placeMotherNatureOnFirstIsland() {
        this.islands.get(0).setMotherNatureStatus(MotherNatureEnum.PRESENT);
    }


    /**
     * returns number of islands
     *
     * @return number of islands
     */
    public int numberOfIslands() {
        return islands.size();
    }

    protected int findMotherNatureType(MotherNatureEnum motherNatureType) throws MissingMotherNatureException {
        for (int i = 0; i < this.islands.size(); i++) {
            if (this.islands.get(i).
                    getMotherNatureStatus().
                    equals(motherNatureType)) return i;
        }
        throw new MissingMotherNatureException();
    }

    /**
     * moves motherNature by the requested amount
     *
     * @param toMove requested movement
     * @throws NegativeInputException thrown when input is below zero
     */
    public void moveMotherNature(int toMove) throws NegativeInputException {
        if (toMove < 0) throw new NegativeInputException("You cannot move a negative amount");
        int currPos = findMotherNatureType(MotherNatureEnum.PRESENT);
        setIslandMotherNature(currPos, MotherNatureEnum.ABSENT);
        setIslandMotherNature(currPos + toMove, MotherNatureEnum.PRESENT);
        notifyObservers(new MoveMotherNatureEvent(currPos, findMotherNatureType(MotherNatureEnum.PRESENT)));
    }

    /**
     * adds temp mother nature on the requested Island
     *
     * @param islandIndex requested island's index
     */
    public void addTempMotherNature(int islandIndex) {
        setIslandMotherNature(islandIndex,
                MotherNatureEnum.max(MotherNatureEnum.TEMP, islands.get(islandIndex).getMotherNatureStatus()));
    }

    private void setIslandMotherNature(int index, MotherNatureEnum motherNature) {
        this.islands.get(index).setMotherNatureStatus(motherNature);
    }

    /**
     * adds given tokenCollection's content on the requested island
     *
     * @param tokenCollection tokenCollection to add
     * @param islandToAddOn   island's index
     */
    public void addTokens(TokenCollection tokenCollection, int islandToAddOn) {
        this.islands.get(islandToAddOn).addTokens(tokenCollection);
    }

    /**
     * Adds the token in the requested island
     *
     * @param tokenEnum     tokenCollection to add
     * @param islandToAddOn island's index
     */
    public void addToken(TokenEnum tokenEnum, int islandToAddOn) {
        this.islands.get(islandToAddOn).addToken(tokenEnum);
    }

    /**
     * returns a copy of the tokenCollection on the requested island
     *
     * @param islandIndex requested island's index
     * @return copy of the requested tokenCollection
     */
    public TokenCollection getIslandTokens(int islandIndex) {
        return islands.get(islandIndex).getTokens().copy();
    }

    /**
     * <p>replaces the selected island's tower colour with the desired one.</p>
     * <p>it also ensures the previous towers are correctly return to their owner</p>
     * <p>while the new towers are taken from the new owner</p>
     *
     * @param islandIndex island on which the switch has to be made
     * @param newColour   desired new island's tower colour
     * @throws GameShouldBeOverException thrown when the player's number of tower falls below zero during switch
     */
    protected void changeIslandTowers(int islandIndex, TowerEnum newColour) throws GameShouldBeOverException {
        Island currentIsland = islands.get(islandIndex);
        returnTowers(currentIsland);
        placeNewTowers(newColour, currentIsland);
    }

    public void changeCurrentIslandTowersWithThoseOfTheWinner(String winningTeam) {
        int currentIslandIndex = getCurrMotherNaturePos();
        Optional<TowerEnum> newColour = accessTeam(winningTeam).stream().map(Team::getTowerColour).findFirst();
        newColour.ifPresent(colour -> changeIslandTowers(currentIslandIndex, colour));
    }

    protected Optional<Team> accessTeamOf(String player) {
        return teams
                .values()
                .stream()
                .filter(team -> team.getPlayers().contains(player))
                .findFirst()
                ;
    }

    /**
     * returns a copy of the team object containing the player
     *
     * @param player player whose team is to be found
     * @return optional containing the team, if present
     */
    // TODO : test thoroughly
    public Optional<Team> getTeamOf(String player) {
        return accessTeamOf(player)
                .map(Team::copy)
                ;
    }

    private void returnTowers(Island currentIsland) {
        ownerOfTowers(currentIsland.getTower()).
                ifPresent(team -> {
                    team.addTowers(currentIsland.getSize());
                    currentIsland.setTowerColour(TowerEnum.NONE);
                    team.getPlayers().forEach(player->notifyObservers(new TowersToDashBoardEvent(team.getTowerColour(), player, islands.indexOf(currentIsland))));
                });
    }

    private void placeNewTowers(TowerEnum newColour, Island currentIsland) {
        ownerOfTowers(newColour)
                .ifPresent(team -> {
                    currentIsland.setTowerColour(newColour);
                    try {
                        team.takeTowers(currentIsland.getSize());
                        team.getPlayers()
                                .forEach(
                                        player->notifyObservers(
                                                new TowersToIslandEvent(
                                                        newColour,
                                                        player,
                                                        islands.indexOf(currentIsland)
                                                )));
                    } catch (NegativeAmountException e) {
                        throw new GameShouldBeOverException();
                    }

                });
    }


    protected Optional<Team> ownerOfTowers(TowerEnum tower) throws NoneTowerException {
        return teams
                .values()
                .stream()
                .filter(team -> team.getTowerColour().equals(tower))
                .findFirst();
    }

    /**
     * <p>Forces the merging of islands.</p>
     * <p>Two islands merge when they are adjacent and have the same tower colour</p>
     */
    public void mergeIslands() {
        int idx = 0;
        while (idx < islands.size()) {

            Island currentIsland = islands.get(idx);
            Island nextIsland = islands.get(idx + 1);
            //if the islands are mergeable we must stay put and check if the next island is compatible as well
            if (currentIsland.isMergeableWith(nextIsland)) {
                currentIsland.inject(nextIsland);
                islands.remove(nextIsland);
                notifyObservers(new MergedIslandsEvent(idx, (idx + 1) % islands.size()));
            }
            //if the island is not mergeable with the next, our work for the island is done; thus we can continue the merging on the next
            else {
                idx++;
            }
        }
    }

    //WIP, might be useful down the line
    public int getCurrMotherNaturePos() throws MissingMotherNatureException {
        if (hasTempMN()) {
            return findMotherNatureType(MotherNatureEnum.TEMP);
        } else {
            return findMotherNatureType(MotherNatureEnum.PRESENT);
        }
    }

    //TODO : test thoroughly
    public List<Team> getTeamsCopy() {
        return teams.values().stream()
                .map(Team::copy)
                .toList();
    }

    public Optional<Team> ownerOfTowersCopy(TowerEnum colour) {
        return ownerOfTowers(colour).map(Team::copy);
    }

    public Island getCurrentIslandCopy() {
        return accessIsland(getCurrMotherNaturePos()).copy();
    }

    public boolean gameOverCondition() {
        return islands.size() <= 3 || teamWithNoTowers();
    }

    private boolean teamWithNoTowers() {
        for (Team team : teams.values()) {
            if (team.getNumberOfTowers() == 0) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IslandChain that = (IslandChain) o;
        return Objects.equals(teams, that.teams) && Objects.equals(islands, that.islands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teams, islands);
    }

    @Override
    public String toString() {
        return "IslandChain{" +
                "teams=" + teams +
                ", islands=" + islands +
                '}';
    }

    //These methods are only used for testing
    protected List<Team> accessTeams() {
        return this.teams.values().stream().toList();
    }

    protected Optional<Team> accessTeam(String teamString) {
        return Optional.ofNullable(teams.get(teamString));
    }


    private boolean hasTempMN() {
        try {
            findMotherNatureType(MotherNatureEnum.TEMP);
            return true;
        } catch (MissingMotherNatureException e) {
            return false;
        }
    }

    public Island accessIsland(int islandIndex) {
        return islands.get(islandIndex);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        addIslandsToDTO(gameDTO);
        addInfoToDashBoards(gameDTO);
    }

    private void addIslandsToDTO(GameDTO gameDTO) {
        List<IslandDTO> dtoIslands = IntStream
                .range(0, islands.size())
                .mapToObj(i -> islands.get(i).getDTO(i))
                .collect(Collectors.toList()); // ignore warnings
        gameDTO.setIslands(dtoIslands);
    }

    private void addInfoToDashBoards(GameDTO gameDTO) {
        teams.values().forEach(team -> team.fillDTO(gameDTO));
    }
}
