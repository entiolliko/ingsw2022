package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.controller.data_transfer_objects.IslandDTO;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.exceptions.ConflictingTowersException;

import java.util.Objects;


/***
 * <p>The class Island has:</p>
 * <p>a size, indicating the number of smaller islands that compose it</p>
 * <p>the colour of the present tower</p>
 * <p>whether it contains mother nature ot not</p>
 * <p>A collection of the tokens that are present on the island</p>
 */
public class Island {

    private final TokenCollection tokens;
    private int size;
    private TowerEnum towerColour;
    private MotherNatureEnum motherNatureStatus;

    /**
     * constructor is hidden, thus a factory method is promoted
     *
     * @param size        size of the Islands
     * @param towerColour colour of the tower
     * @param hasMn       whether it contains motherNature
     * @param tokens      the tokenCollection given to the Island(which is copied)
     */
    private Island(int size, TowerEnum towerColour, MotherNatureEnum hasMn, TokenCollection tokens) {
        if (size < 0) throw new IllegalArgumentException();
        this.size = size;
        this.towerColour = towerColour;
        this.motherNatureStatus = hasMn;
        this.tokens = tokens.copy();
    }

    /***
     * zero arguments constructor is hidden as well
     */
    private Island() {
        this(1, TowerEnum.NONE, MotherNatureEnum.ABSENT, TokenCollection.newEmptyCollection());
    }

    /***
     * creates an Island of size 1 with nothing in it
     * @return an empty island of size 1
     */
    public static Island newEmptyIsland() {
        return new Island();
    }

    /***
     * does little more than calling the constructor
     * @param size size of the Islands
     * @param towerColour colour of the tower
     * @param hasMn whether it contains motherNature
     * @param tokens the tokenCollection given to the Island(which is copied)
     * @return an Island object conforming to the desired arguments
     */
    protected static Island createIsland(int size, TowerEnum towerColour, MotherNatureEnum hasMn, TokenCollection tokens) {
        return new Island(size, towerColour, hasMn, tokens);
    }

    /***
     * returns a copy of the called Island
     * @return a copy of the called Island
     */
    public Island copy() {
        return new Island(this.size, this.towerColour, this.motherNatureStatus, this.tokens);
    }

    /***
     * recolours the present towers
     * @param towerColour new colour of the towers on the island
     */
    public void changeTowerColour(TowerEnum towerColour) {
        this.towerColour = towerColour;
    }

    /***
     * returns the size of the Island
     * @return the size of the Island
     */
    public int getSize() {
        return this.size;
    }

    /***
     * returns the towers colour
     * @return the towers colour
     */
    public TowerEnum getTower() {
        return this.towerColour;
    }

    /**
     * returns the presence status of Mother Nature
     *
     * @return the presence status of Mother Nature
     */
    public MotherNatureEnum getMotherNatureStatus() {
        return this.motherNatureStatus;
    }

    /***
     * modifies the presence status of Mother Nature
     * @param motherNatureStatus new status of Mother Nature in the Island
     */
    public void setMotherNatureStatus(MotherNatureEnum motherNatureStatus) {
        this.motherNatureStatus = motherNatureStatus;
    }

    /**
     * returns a TokenCollection equivalent to the one present in the Island
     *
     * @return a copy of the tokens attribute
     */
    public TokenCollection getTokens() {
        return this.tokens.copy();
    }

    /**
     * <p>takes all the contents of the passed Island and injects them in the called Island</p>
     * <p>without altering the passed Island.</p>
     * <p>This method is used by IslandChain for merging Islands</p>
     *
     * @param toInject Island whose content is to be injected
     */
    public void inject(Island toInject) {
        if (!this.towerColour.equals(toInject.towerColour)) throw new ConflictingTowersException();
        this.size += toInject.size;
        this.motherNatureStatus = MotherNatureEnum.max(this.getMotherNatureStatus(), toInject.getMotherNatureStatus());
        this.addTokens(toInject.getTokens());

    }

    /***
     * method used by IslandChain to add tokens to island's TokenCollection
     * @param toAdd tokenCollection to add
     */
    public void addTokens(TokenCollection toAdd) {
        this.tokens.addToCollection(toAdd);
    }

    /***
     * method used by IslandChain to add a token to island's TokenCollection
     * @param toAdd The token to be added
     */
    public void addToken(TokenEnum toAdd) {
        this.tokens.addTokens(toAdd, 1);
    }

    /**
     * sets the colour of the tower to the desired parameter
     *
     * @param towerColour desire colour for the island's tower
     */
    public void setTowerColour(TowerEnum towerColour) {
        this.towerColour = towerColour;
    }

    public boolean isMergeableWith(Island toMergeWith) {
        TowerEnum myTower = this.getTower();
        TowerEnum guestTower = toMergeWith.getTower();
        return !myTower.equals(TowerEnum.NONE) && myTower.equals(guestTower);
    }

    /***
     * default equal method
     * @param o confronted object
     * @return whether the objects are in the same equivalence class or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return size == island.size && towerColour == island.towerColour && motherNatureStatus == island.motherNatureStatus && Objects.equals(tokens, island.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, towerColour, motherNatureStatus, tokens);
    }

    public IslandDTO getDTO(int index) {
        IslandDTO simpleIsland = new IslandDTO();
        simpleIsland.setIndex(index);
        simpleIsland.setSize(size);
        simpleIsland.setHasMotherNature(motherNatureStatus.equals(MotherNatureEnum.PRESENT));
        simpleIsland.setTokens(tokens.getMap());
        simpleIsland.setTowerColour(towerColour);
        return simpleIsland;
    }
}
