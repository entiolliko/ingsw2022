package it.polimi.ingsw.controller.data_transfer_objects;

import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.TowerEnum;

import java.util.Map;

public class IslandDTO {
    private int index;
    private int size;

    private boolean hasMotherNature;

    private Map<TokenEnum, Integer> tokens;

    private TowerEnum towerColour;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public Map<TokenEnum, Integer> getTokens() {
        return tokens;
    }

    public void setTokens(Map<TokenEnum, Integer> tokens) {
        this.tokens = tokens;
    }

    public TowerEnum getTowerColour() {
        return towerColour;
    }

    public void setTowerColour(TowerEnum towerColour) {
        this.towerColour = towerColour;
    }

    @Override
    public String toString() {
        return "\tIslandDTO{" + "\n\t" +
                "index=" + index + "\n\t" +
                ", size=" + size + "\n\t" +
                ", hasMotherNature=" + hasMotherNature + "\n\t" +
                ", tokens=" + tokens + "\n\t" +
                ", towerColour=" + towerColour + "\n\t" +
                '}';
    }
}
