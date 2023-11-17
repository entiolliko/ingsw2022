package it.polimi.ingsw.controller.data_transfer_objects;

import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Map;

public class CharacterCardDTO {

    private boolean isActive;
    private String cardName;
    private Integer cost;
    private Map<TokenEnum, Integer> tokens;
    private String effect;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Map<TokenEnum, Integer> getTokens() {
        return tokens;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setTokens(Map<TokenEnum, Integer> tokens) {
        this.tokens = tokens;
    }
}
