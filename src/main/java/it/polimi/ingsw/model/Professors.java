package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.data_transfer_objects.DashboardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.DashBoard;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.ProfessorChangedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * class to manage professors tokens
 */
public class Professors extends ModelEventCreator implements Simplifiable {
    EnumMap<TokenEnum, String> owners;
    //player that wins even if token number is a draw
    String playerWithBonus;

    /**
     * constructor
     */
    public Professors() {
        this.owners = new EnumMap<>(TokenEnum.class);
        Stream.of(TokenEnum.values()).forEach(token -> owners.put(token, null));
        this.playerWithBonus = null;
    }

    private Professors(@NotNull Professors toCopy) {
        this.owners = new EnumMap<>(toCopy.owners);
        String strCopy = String.valueOf(toCopy.playerWithBonus);
        this.playerWithBonus = (Objects.isNull(toCopy.playerWithBonus)) ? null : strCopy;
    }

    /**
     * get the Map containing the owners
     *
     * @return Map containing for each token the owner
     */
    public Map<TokenEnum, String> getOwners() {
        return new EnumMap<>(this.owners);
    }

    private void setOwners(EnumMap<TokenEnum, String> owners) {
        this.owners = owners;
    }

    /**
     * method to return the owner of a given token
     *
     * @param token the token that belongs to the owner
     * @return the owner that owns the token
     */
    public String getOwner(TokenEnum token) {
        return (Objects.isNull(this.owners.get(token))) ? null : String.valueOf(this.owners.get(token));
    }

    /**
     * method to create a copy of Professors obj
     *
     * @return a copy of Professors
     */
    public Professors copy() {
        return new Professors(this);
    }

    /**
     * actions to set next turn, resets the player with bonus to null
     */
    public void prepareForNextRound() {
        this.playerWithBonus = null;
    }
    public void prepareForNextTurn(){
        this.playerWithBonus = null;
    }

    /**
     * method to set the "Win token even if result is draw" effect to a player
     *
     * @param playerID the player who needs the bonus
     */
    public void setPlayerWithBonus(String playerID) {
        this.playerWithBonus = playerID;
    }

    public void updateProfessors(Map<String, DashBoard> dashboards) {
        Map<String, TokenCollection> studyHalls = new HashMap<>();
        for (Map.Entry<String, DashBoard> db : dashboards.entrySet()) {
            studyHalls.put(db.getKey(), db.getValue().cloneStudyHall());
        }
        updateProfessorStatus(studyHalls);
    }

    /**
     * method to update the internal status of professors, according to game rules
     *
     * @param dashBoardsStatus Map between players and StudyHalls
     */
    public void updateProfessorStatus(Map<String, TokenCollection> dashBoardsStatus) {

        Map<TokenEnum, Integer> maxToken = new EnumMap<>(TokenEnum.class);
        this.owners.forEach((token, playerID) -> maxToken.put(token, (Objects.isNull(playerID)) ? 0 : dashBoardsStatus.get(playerID).get(token)));

        EnumMap<TokenEnum, String> ownersCopy = new EnumMap<>(this.owners);

        // scans trough players, if a player has more token gets the professor, if bonus even with draw
        dashBoardsStatus.forEach((playerID, tokColl) -> owners.keySet()
                .forEach(token ->
                        {
                            if (playerID.equals(playerWithBonus)) {
                                if (tokColl.get(token) >= maxToken.get(token) && tokColl.get(token)>0) {
                                    this.owners.put(token, playerID);
                                    maxToken.put(token, tokColl.get(token));
                                }
                            } else {
                                if (tokColl.get(token) > maxToken.get(token)) {
                                    this.owners.put(token, playerID);
                                    maxToken.put(token, tokColl.get(token));
                                }
                            }
                        }
                ));

        this.notifyChanges(ownersCopy);

    }

    //check if any token has changed owner if so sends a game event
    private void notifyChanges(EnumMap<TokenEnum, String> oldOwners){
        oldOwners.forEach(
                (token, oldPlayer)->{
                    if (Objects.isNull(oldPlayer)){
                        if (!Objects.isNull(owners.get(token))){
                            this.notifyObservers(new ProfessorChangedEvent(token, owners.get(token)));
                        }
                    }
                    else if (!oldPlayer.equals(owners.get(token))){
                        this.notifyObservers(new ProfessorChangedEvent(token, owners.get(token)));
                    }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professors that = (Professors) o;
        return Objects.equals(owners, that.owners) && Objects.equals(playerWithBonus, that.playerWithBonus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owners, playerWithBonus);
    }

    @Override
    public String toString() {
        return "Professors{" +
                "owners=" + owners +
                ", playerWithBonus='" + playerWithBonus + '\'' +
                '}';
    }


    @Override
    public void fillDTO(GameDTO gameDTO) {
        Map<String, DashboardDTO> dashboardDTOs = gameDTO.getDashboards();
        for (Map.Entry<TokenEnum, String> professorOwner : owners.entrySet()) {
            if (!Objects.isNull(professorOwner.getValue())) {
                dashboardDTOs
                        .get(professorOwner
                                .getValue())
                        .getProfessors()
                        .add(professorOwner
                                .getKey())
                ;
            }
        }
    }
}