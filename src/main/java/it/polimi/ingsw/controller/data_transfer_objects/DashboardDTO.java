package it.polimi.ingsw.controller.data_transfer_objects;

import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.TowerEnum;

import java.util.*;

public class DashboardDTO {

    private String owner;
    private Map<TokenEnum, Integer> entranceHall;
    private Map<TokenEnum, Integer> studyHall;
    private Integer towers;
    private TowerEnum towerColour;
    private Integer coins;
    private Set<TokenEnum> professors;

    private List<Integer> theHand;
    private Deque<Integer> playedCards;


    private boolean hasPlayedCard;

    public DashboardDTO() {
        professors = new HashSet<>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<TokenEnum, Integer> getEntranceHall() {
        return entranceHall;
    }

    public void setEntranceHall(Map<TokenEnum, Integer> entranceHall) {
        this.entranceHall = entranceHall;
    }

    public Map<TokenEnum, Integer> getStudyHall() {
        return studyHall;
    }

    public void setStudyHall(Map<TokenEnum, Integer> studyHall) {
        this.studyHall = studyHall;
    }

    public Integer getTowers() {
        return towers;
    }

    public void setTowers(Integer towers) {
        this.towers = towers;
    }

    public TowerEnum getTowerColour() {
        return towerColour;
    }

    public void setTowerColour(TowerEnum towerColour) {
        this.towerColour = towerColour;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Set<TokenEnum> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<TokenEnum> professors) {
        this.professors = professors;
    }

    public List<Integer> getTheHand() {
        return theHand;
    }

    public void setTheHand(List<Integer> theHand) {
        this.theHand = theHand;
    }

    public Deque<Integer> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(Deque<Integer> playedCards) {
        this.playedCards = playedCards;
    }

    public boolean hasPlayedCard() {
        return hasPlayedCard;
    }

    public void setHasPlayedCard(boolean hasPlayedCard) {
        this.hasPlayedCard = hasPlayedCard;
    }

    @Override
    public String toString() {
        return "DashboardDTO{" +
                "owner='" + owner + '\'' +
                ", entranceHall=" + entranceHall +
                ", studyHall=" + studyHall +
                ", towers=" + towers +
                ", towerColour=" + towerColour +
                ", coins=" + coins +
                ", professors=" + professors +
                ", theHand=" + theHand +
                ", playedCards=" + playedCards +
                '}';
    }
}
