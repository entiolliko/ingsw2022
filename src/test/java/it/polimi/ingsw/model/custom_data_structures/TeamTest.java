package it.polimi.ingsw.model.custom_data_structures;

import it.polimi.ingsw.model.custom_data_structures.exceptions.EmptyException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.islands.exceptions.NoneTowerException;
import it.polimi.ingsw.model.islands.TowerEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldHaveTheCorrectName(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        assertEquals(name, new Team(name, players, colour, numberOfTowers).getName());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldHaveTheDesiredPlayers(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        assertEquals(players, new Team(name, players, colour, numberOfTowers).getPlayers());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldHaveTheCorrectTowerColour(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        assertEquals(colour, new Team(name, players, colour, numberOfTowers).getTowerColour());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldStartWithTheCorrectNumberOfTowers(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        assertEquals(numberOfTowers, new Team(name, players, colour, numberOfTowers).getNumberOfTowers());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void constructorShouldFailWithZeroPlayersTeam(String name, List<String> players,TowerEnum colour, int numberOfTowers) {
        players = List.of();
        List<String> finalPlayers = players;
        assertThrows(EmptyException.class, () -> new Team(name, finalPlayers, colour, numberOfTowers));
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void constructorShouldFailWithNoTowerEnum(String name, List<String> players, TowerEnum badColour, int numberOfTowers) {
        badColour = TowerEnum.NONE;
        TowerEnum finalBadColour = badColour;
        assertThrows(NoneTowerException.class, () -> new Team(name, players, finalBadColour, numberOfTowers));
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void constructorShouldFailWithNegativeNumberOFTowers(String name, List<String> players, TowerEnum colour, int badNumberOfTowers) {
        badNumberOfTowers *= -1;
        int finalBadNumberOfTowers = badNumberOfTowers;
        assertThrows(NegativeInputException.class, ()-> new Team(name, players, colour, finalBadNumberOfTowers));
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldSaveACopyOfTheList(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        List<String> playersCopy = new ArrayList<>(players);

        Team tested = new Team(name, playersCopy, colour, numberOfTowers);
        //modify the playersCopy list
        //if the lists are different, then it's certain that teams stored a copy instead of the reference to the list
        playersCopy.add("uncle xi");

        assertNotEquals(playersCopy, tested.getPlayers());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void teamShouldReturnACopyOfItsPlayersList(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        Team tested = new Team(name, players, colour, numberOfTowers);

        List<String> copy = tested.getPlayers();
        copy.add("uncle xi");

        assertNotEquals(copy, tested.getPlayers());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void getNumberOfTowersWorksProperly(String name, List<String> players, TowerEnum colour, int numberOfTowers) throws NoneTowerException {
        assertEquals(numberOfTowers, new Team(name, players, colour, numberOfTowers).getNumberOfTowers());
    }
    @ParameterizedTest
    @MethodSource({"addTestSuite", "addZeroTestSuite"})
    void addTowersShouldIncreaseNumberOfTowersCorrectly(Team testedTeam, int amountToAdd) {
        int originalAmount = testedTeam.getNumberOfTowers();
        testedTeam.addTowers(amountToAdd);
        assertEquals(originalAmount + amountToAdd, testedTeam.getNumberOfTowers());
    }
    @ParameterizedTest
    @MethodSource("addTestSuite")
    void addTowersShouldNotAcceptNegativeInput(Team testedTeam, int amountToAdd) {
        assertThrows(NegativeInputException.class, () -> testedTeam.addTowers(-amountToAdd));
    }
    @ParameterizedTest
    @MethodSource({"takeTowersTestSuite", "takeTowersCornerCaseTestSuite"})
    void takeTowersShouldWorkWithNormalValues(Team testedTeam, int amountToTake) throws NegativeAmountException {
        int originalAmount = testedTeam.getNumberOfTowers();
        testedTeam.takeTowers(amountToTake);
        assertEquals(originalAmount - amountToTake, testedTeam.getNumberOfTowers());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void copyShouldReturnBeEquivalentToTheOriginal(String name, List<String> players, TowerEnum colour, int numberOfTowers) {
        Team tested = new Team(name, players, colour, numberOfTowers);
        assertEquals(tested, tested.copy());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void copyShouldNotReturnTheObjectItself(String name, List<String> players, TowerEnum colour, int numberOfTowers) {
        Team tested = new Team(name, players, colour, numberOfTowers);
        assertNotSame(tested, tested.copy());
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void copiedTeamShouldNotAllowModificationOfTheSameObject(String name, List<String> players, TowerEnum colour, int numberOfTowers) {
        Team tested = new Team(name, players, colour, numberOfTowers);
        Team copy = tested.copy();

        copy.addTowers(1);
        assertNotEquals(tested, copy);
    }
    @ParameterizedTest
    @MethodSource("defaultTestSuite")
    void copiedTeamsShouldHaveTheSameHashCode(String name, List<String> players, TowerEnum colour, int numberOfTowers) {
        Team tested = new Team(name, players, colour, numberOfTowers);
        Team copy = tested.copy();
        assertEquals(tested.hashCode(), copy.hashCode());
    }




    public static Stream<Arguments> defaultTestSuite() {
        List<Arguments> suite = new ArrayList<>();
        suite.add(Arguments.of("Yan' an", List.of("xue"),TowerEnum.WHITE , 8));
        suite.add(Arguments.of("Beijing", List.of("hua"), TowerEnum.GREY, 6));
        suite.add(Arguments.of("Nanjing", List.of("xue", "hua"), TowerEnum.BLACK, 8));
        return suite.stream();
    }
    public static List<Team> preMadeTeams() {
        return List.of(
                new Team("Yan' an", List.of("xue"), TowerEnum.WHITE, 8),
                new Team("Beijing", List.of("xue, hua"), TowerEnum.BLACK, 8),
                new Team("Nanjing", List.of("xi"), TowerEnum.GREY, 6)
        );
    }
    public static Stream<Arguments> addZeroTestSuite() {
        List<Arguments> suite = new ArrayList<>();
        for (Team team : preMadeTeams()) {
            suite.add(Arguments.of(team, 0));
        }
        return suite.stream();
    }
    public static Stream<Arguments> addTestSuite() {
        List<Arguments> suite = new ArrayList<>();
        for (Team team : preMadeTeams()) {
            suite.add(Arguments.of(team, new Random().nextInt(1,10)));
        }
        return suite.stream();
    }
    public static Stream<Arguments> takeTowersTestSuite() {
        List<Arguments> suite = new ArrayList<>();
        for (Team team : preMadeTeams()) {
            suite.add(Arguments.of(team, new Random().nextInt(team.getNumberOfTowers())));
        }
        return suite.stream();
    }
    public static Stream<Arguments> takeTowersCornerCaseTestSuite() {
        List<Arguments> suite = new ArrayList<>();
        for (Team team : preMadeTeams()) {
            suite.add(Arguments.of(team, team.getNumberOfTowers()));
        }
        return suite.stream();
    }
}
