package it.polimi.ingsw.controller.data_transfer_objects;

import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDTO {
    private String gamePhase;
    private String currentPlayer;

    private int bagSize;
    private List<Map<TokenEnum, Integer>> clouds;
    private List<IslandDTO> islands;
    private Map<String, DashboardDTO> dashboards;
    private List<CharacterCardDTO> characterCards;

    public GameDTO(List<String> players) {
        this.clouds = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.dashboards = new HashMap<>();
        createEmptyDashBoards(players);
        this.characterCards = new ArrayList<>();
    }

    private void createEmptyDashBoards(List<String> players) {
        for (String player : players) {
            dashboards.put(player, new DashboardDTO());
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(String gamePhase) {
        this.gamePhase = gamePhase;
    }

    public int getBagSize() {
        return bagSize;
    }

    public void setBagSize(int bagSize) {
        this.bagSize = bagSize;
    }

    public List<Map<TokenEnum, Integer>> getClouds() {
        return clouds;
    }

    public void setClouds(List<Map<TokenEnum, Integer>> clouds) {
        this.clouds = clouds;
    }

    public List<IslandDTO> getIslands() {
        return islands;
    }

    public void setIslands(List<IslandDTO> islands) {
        this.islands = islands;
    }

    public Map<String, DashboardDTO> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Map<String, DashboardDTO> dashboards) {
        this.dashboards = dashboards;
    }

    public List<CharacterCardDTO> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(List<CharacterCardDTO> characterCards) {
        this.characterCards = characterCards;
    }

    @Override
    public String toString() {
        return "GameDTO{\n" +
                "gamePhase='" + gamePhase + '\'' + '\n' +
                ", currentPlayer='" + currentPlayer + '\'' + '\n' +
                ", bagSize=" + bagSize + '\n' +
                ", clouds=" + clouds + '\n' +
                ", islands=" + islands + '\n' +
                ", dashboards=" + dashboards + '\n' +
                ", characterCards=" + characterCards + '\n' +
                '}';
    }
}
