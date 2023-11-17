package it.polimi.ingsw.model;


import it.polimi.ingsw.model.custom_data_structures.IntegerStack;
import it.polimi.ingsw.model.custom_data_structures.Team;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.IslandChain;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class InfluenceHandler {
    private final EnumMap<TokenEnum, Boolean> offlineProfessors;
    private String bonusPlayer;
    private Integer bonusValue;
    private boolean offlineTowers;

    public InfluenceHandler() {
        offlineProfessors = new EnumMap<>(TokenEnum.class);
        this.prepareForNextRound();
    }

    /**
     * Adds the bonus to the player for the character card
     * @param bonusPlayer
     * @param bonusValue
     */
    public void addBonus(String bonusPlayer, Integer bonusValue) {
        this.bonusPlayer = bonusPlayer;
        this.bonusValue = bonusValue;
    }

    /**
     * The professor to not be calculated for the influence
     * @param token The professor to be shut downed
     */
    public void shutDownProfessor(TokenEnum token) {
        this.offlineProfessors.put(token, true);
    }

    /**
     * The towers are not calculated in the influence
     */
    public void shutDownTowers() {
        this.offlineTowers = true;
    }

    /**
     * Prepares the attributes for the next round
     */
    public void prepareForNextRound() {
        prepareForNextTurn();
    }

    /**
     * Prepares the attributes for the next turn
     */
    public void prepareForNextTurn() {
        Stream.of(TokenEnum.values()).forEach(token -> offlineProfessors.put(token, false));
        this.offlineTowers = false;
        this.bonusPlayer = null;
        this.bonusValue = null;
    }

    /*
        public String winner(IslandChain chain, TokenCollection islandTokens, String towerOwner, Integer towerNumber, @NotNull Professors prof) {

            //get unique team values
            List<String> teams = List.of(String.valueOf(Set.of(prof.getOwners().values().add(towerOwner))));

            Map<String, Integer> influences = new HashMap<>();

            teams.forEach( team ->{

                int influence = 0;

                influence += b2i(!offlineTowers) * b2i(towerOwner.equals(team)) * towerNumber;

                for (TokenEnum token : TokenEnum.values()) {
                    influence += b2i(team.equals(chain.getTeamOf(prof.getOwner(token)).get().getName())) * b2i(!offlineProfessors.get(token)) * islandTokens.get(token);
                }

                influence += b2i(bonusTeam.equals(team)) * bonusValue;

                influences.put(team, influence);
            }
            );
            return Collections.max(influences.entrySet(), Map.Entry.comparingByValue()).getKey();
        }

     */

    /**
     * Returns the winner in the current game configuration
     * @param islands The list of the islands
     * @param professors The professors
     * @return The name of the winner
     */
    public String winner(IslandChain islands, Professors professors) {
        Map<String, IntegerStack> scores = getScores(islands, professors);
        return getWinner(scores);
    }

    /**
     * Return the scores for each player
     * @param islands The list of the islands
     * @param professors The professors
     * @return The score for each player
     */
    @NotNull
    protected Map<String, IntegerStack> getScores(IslandChain islands, Professors professors) {
        Map<String, IntegerStack> scores = emptyScoreBoard(islands);
        if (!offlineTowers)
            addTowersToScore(scores, islands);
        addTokensToScore(islands, professors, scores);
        addCardBonus(scores, islands);
        return scores;
    }

    /**
     * Resets the score for each player
     * @param islands The list of the islands
     * @return The score for each player
     */
    private Map<String, IntegerStack> emptyScoreBoard(IslandChain islands) {
        Map<String, IntegerStack> scores = new HashMap<>();
        for (Team team : islands.getTeamsCopy()) {
            scores.put(team.getName(), new IntegerStack());
        }
        return scores;
    }

    /**
     * Adds the towers to the score calculation
     * @param scores The current scores
     * @param islands The list of the islands
     */
    private void addTowersToScore(Map<String, IntegerStack> scores, IslandChain islands) {
        Island currentIsland = islands.getCurrentIslandCopy();
        Optional<Team> ownerOfTowers = islands.ownerOfTowersCopy(currentIsland.getTower());
        ownerOfTowers.ifPresent(
                team -> scores.get(team.getName()).add(currentIsland.getSize())
        );
    }

    private void addTokensToScore(IslandChain islands, Professors professors, Map<String, IntegerStack> scores) {
        Island currentIsland = islands.getCurrentIslandCopy();
        for (TokenEnum colour : TokenEnum.values()) {
            if (Boolean.FALSE.equals(this.offlineProfessors.get(colour))) {
                String ownerOfProf = professors.getOwner(colour);
                Optional<Team> ownerTeam = islands.getTeamOf(ownerOfProf);
                ownerTeam.ifPresent(
                        team -> scores.get(team.getName()).add(currentIsland.getTokens().get(colour))
                );
            }
        }
    }

    private void addCardBonus(Map<String, IntegerStack> scores, IslandChain islands) {
        if (!Objects.isNull(bonusPlayer)) {
            islands.getTeamOf(bonusPlayer)
                    .ifPresent(
                            team -> scores
                                    .get(team.getName())
                                    .add(bonusValue));
        }
    }

    private String getWinner(Map<String, IntegerStack> scores) {

        int highestScore = scores
                .values()
                .stream()
                .mapToInt(IntegerStack::size)
                .max()
                .orElse(0);
        List<Map.Entry<String, IntegerStack>> winners = scores
                .entrySet()
                .stream()
                .filter(team -> team.getValue().size() >= highestScore)
                .toList();
        if (winners.size() == 1) return winners.get(0).getKey();
        else return null; // in case of ambiguity the status quo is preserved
    }

    @Contract(pure = true)
    private @NotNull Integer b2i(boolean value) {
        return (value) ? 1 : 0;
    }
}
