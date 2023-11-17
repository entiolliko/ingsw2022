package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.model.custom_data_structures.Team;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeInputException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.SameNameException;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.islands.exceptions.GameShouldBeOverException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IslandChainTest extends IslandChain{


    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandChainShouldHave12Islands(Map<String, List<String>> TeamsToPlayers) {
        assertEquals(12, IslandChain.newDefaultIslandChain(TeamsToPlayers).numberOfIslands());
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandChainShouldAllowTeamsOfOneAndTwoPlayers(Map<String, List<String>> TeamsToPlayers) {
        assertDoesNotThrow(() -> IslandChain.newDefaultIslandChain(TeamsToPlayers));
    }

    @ParameterizedTest
    @MethodSource("wrongTeamSizesSuite")
    void newDefaultIslandShouldNotAcceptInvalidNumbersOfPlayers(Map<String, List<String>> TeamsToPlayers){
        assertThrows(IllegalArgumentException.class, () -> IslandChain.newDefaultIslandChain(TeamsToPlayers));
    }



    @ParameterizedTest
    @MethodSource("sameTeamHomonymSuite")
    void newDefaultIslandChainShouldNotAcceptHomonymPlayersInTheSameTeam(HashMap<String, List<String>> badTeamsToPlayers) {
        assertThrows(SameNameException.class, () -> IslandChain.newDefaultIslandChain(badTeamsToPlayers));
    }

    @ParameterizedTest
    @MethodSource("differentTeamHomonymSuite")
    void newDefaultIslandChainShouldNotAcceptHomonymPlayersInDifferentTeams(HashMap<String, List<String>> badTeamsToPlayers) {
        assertThrows(SameNameException.class, () -> IslandChain.newDefaultIslandChain(badTeamsToPlayers));
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandChainShouldCreateTeamsWithDifferentTowerColours(Map<String, List<String>> TeamsToPlayers) {
        IslandChain tested = IslandChain.newDefaultIslandChain(TeamsToPlayers);
        List<TowerEnum> towers = tested.accessTeams().stream().map(Team::getTowerColour).toList();
        assertEquals(towers.stream().distinct().toList(),towers);
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandChainShouldHaveMotherNatureInPos0(Map<String, List<String>> TeamsToPlayers) {
        assertEquals(MotherNatureEnum.PRESENT, IslandChain.newDefaultIslandChain(TeamsToPlayers).accessIsland(0).getMotherNatureStatus());
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandShouldNotHaveMotherNatureInIslandsThatAreNot0(Map<String, List<String>> TeamsToPlayers) {
        IslandChain tested = IslandChain.newDefaultIslandChain(TeamsToPlayers);
        for (int i = 1; i < tested.numberOfIslands(); i++) {
            assertEquals(MotherNatureEnum.ABSENT, tested.accessIsland(i).getMotherNatureStatus());
        }
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void newDefaultIslandChainShouldHaveAllItsIslandsWithoutAnyTokens(Map<String, List<String>> TeamsToPlayers) {
        IslandChain tested = IslandChain.newDefaultIslandChain(TeamsToPlayers);
        for (int i = 0; i < tested.numberOfIslands(); i++) {
            assertTrue(tested.getIslandTokens(i).isEmpty());
            assertEquals(0, tested.getIslandTokens(i).size());
        }
    }

    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void equalsTestTrue(Map<String, List<String>> TeamsToPlayers) {
        assertEquals(IslandChain.newDefaultIslandChain(TeamsToPlayers), IslandChain.newDefaultIslandChain(TeamsToPlayers));
    }
    @ParameterizedTest
    @MethodSource({"defaultSuite"})
    void equalsTestFalse(Map<String, List<String>> TeamsToPlayers) {
        Map<String, List<String>> TeamsToPlayersModified = new HashMap<>(TeamsToPlayers);
        TeamsToPlayersModified.put(TeamsToPlayers.keySet().stream().toList().get(0), List.of("OsamaTest"));
        assertNotEquals(IslandChain.newDefaultIslandChain(TeamsToPlayers), IslandChain.newDefaultIslandChain(TeamsToPlayersModified));
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void moveMNShouldWorkProperly(int toMove) {
        IslandChain tested = standardTestCase();
        int originalPos = tested.getCurrMotherNaturePos();
        tested.moveMotherNature(toMove);
        assertEquals((originalPos + toMove) % tested.numberOfIslands(),tested.getCurrMotherNaturePos());
    }
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void moveMNShouldNotAcceptNegativeInputs(int toMove) {
        IslandChain tested = standardTestCase();
        assertThrows(NegativeInputException.class, () -> tested.moveMotherNature(-toMove));
    }
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "13",})
    void moveMNShouldNotLeaveAPresentMotherNatureOnTheOldIsland(int toMove) {
        IslandChain tested = standardTestCase();
        tested.moveMotherNature(toMove);
        assertEquals(MotherNatureEnum.ABSENT, tested.accessIsland(0).getMotherNatureStatus());
    }
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void moveMNShouldOverwriteTempMN(int toMove) {
        IslandChain tested = standardTestCase();
        tested.addTempMotherNature(toMove);
        tested.moveMotherNature(toMove);
        assertEquals(MotherNatureEnum.PRESENT, tested.accessIsland(toMove).getMotherNatureStatus());
    }
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void getCurrMotherNaturePosShouldWorkProperly(int toMove){
        IslandChain tested = standardTestCase();
        tested.moveMotherNature(toMove);
        assertEquals(toMove % tested.numberOfIslands(), tested.getCurrMotherNaturePos());

    }
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void getCurrMotherNaturePosShouldFavorTempMNOverPresentOne(int pos) {
        IslandChain tested = standardTestCase();
        tested.moveMotherNature(pos);
        tested.addTempMotherNature(pos + 1);
        assertEquals((pos + 1) % tested.numberOfIslands(), tested.getCurrMotherNaturePos());
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "150"})
    void addTempMotherNatureShouldNotSwitchPresentWithTemp(int toMove) {
     IslandChain tested = standardTestCase();
     tested.moveMotherNature(toMove); // move MN
     tested.addTempMotherNature(toMove); // try placing MN where MN already is
     assertEquals(MotherNatureEnum.PRESENT, tested.accessIsland(toMove).getMotherNatureStatus());
    }
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "13", "15"})
    void addTempMotherNatureShouldSwitchAbsentWithTemp(int toMove) {
        IslandChain tested = standardTestCase();
        tested.moveMotherNature(toMove);
        tested.addTempMotherNature(toMove + 1); // make testcases so that tempMN isn't placed where true MN is already present
        assertEquals(MotherNatureEnum.TEMP, tested.accessIsland(toMove + 1).getMotherNatureStatus());
    }

    @ParameterizedTest
    @MethodSource("addTokensSuite")
    void addTokensShouldModifyTheSelectedIslandCollectionProperly(int islandToAddOn, TokenCollection tokenCollection) {
        IslandChain tested = standardTestCase();
        tested.addTokens(tokenCollection, islandToAddOn);
        assertEquals(tokenCollection, tested.getIslandTokens(islandToAddOn));
    }
    @ParameterizedTest
    @MethodSource("addTokensSuite")
    void addTokensShouldLeaveTheOtherIslandsUntouched(int islandToAddOn, TokenCollection tokenCollection) {
        IslandChain tested = standardTestCase();
        tested.addTokens(tokenCollection, islandToAddOn);
        for (int i = 0; i < tested.numberOfIslands(); i++) {
            if (i % tested.numberOfIslands() != islandToAddOn % tested.numberOfIslands()) {
                assertTrue(tested.getIslandTokens(i).isEmpty());
                assertEquals(0, tested.getIslandTokens(i).size());
            }
        }
    }
    @ParameterizedTest
    @MethodSource("addTokensSuite")
    void addTokensShouldModifyCurrentCollectionInsteadOfOverWriting(int islandToAddOn, TokenCollection tokenCollection) {
        IslandChain tested = standardTestCase();
        tested.addTokens(tokenCollection, islandToAddOn);
        tested.addTokens(tokenCollection, islandToAddOn);

        TokenCollection expected = TokenCollection.newEmptyCollection();
        expected.addToCollection(tokenCollection);
        expected.addToCollection(tokenCollection);

        assertEquals(expected, tested.getIslandTokens(islandToAddOn));
    }
    @ParameterizedTest
    @CsvSource({"0, WHITE", "1, BLACK", "2, WHITE", "3, BLACK", "12, WHITE"})
    void changeIslandTowersShouldChangeColourAccordingly(int island, TowerEnum colour) {
        IslandChain tested = standardTestCase();

        tested.changeIslandTowers(island, colour);

        assertEquals(colour, tested.accessIsland(island).getTower());
    }
    @ParameterizedTest
    @CsvSource({"0, WHITE", "1, BLACK", "2, WHITE", "3, BLACK", "12, WHITE"})
    void changeIslandTowersShouldRemoveTheCorrectNumberOfTowersFromTeam(int island, TowerEnum colour) {
        IslandChain tested = standardTestCase();
        Optional<Team> newOwner = tested.ownerOfTowers(colour);
        assert newOwner.orElse(null) != null;
        Optional<Integer> previousAmount = Optional.of(newOwner.orElse(null).getNumberOfTowers());

        tested.changeIslandTowers(island, colour);

        assertEquals(previousAmount.get() - tested.accessIsland(island).getSize(), newOwner.get().getNumberOfTowers());
    }
    @ParameterizedTest
    @CsvSource({"0, WHITE, BLACK", "1, BLACK, WHITE", "2, WHITE, BLACK", "3, BLACK, WHITE", "12, WHITE, BLACK"})
    //create test suite with Islands which already have placeTowers
    void changeIslandTowersShouldReturnTheCorrectNumberOfTowersToTheTeam(int island, TowerEnum firstColour, TowerEnum newColour) {
        IslandChain tested = standardTestCase();

        tested.changeIslandTowers(island, firstColour);

        Optional<Team> previousOwner = tested.ownerOfTowers(firstColour);
        assert previousOwner.orElse(null) != null;
        Optional<Integer> originalAmount = Optional.of(previousOwner.orElse(null).getNumberOfTowers());

        tested.changeIslandTowers(island, newColour);

        assertEquals(originalAmount.get() + tested.accessIsland(island).getSize(), previousOwner.get().getNumberOfTowers());
    }

    @ParameterizedTest
    @MethodSource("defaultSuite")
    void changeIslandTowersShouldFailWhenThePlayerNoLongerHasTowers(Map<String, List<String>> testcase) {
        IslandChain tested = IslandChain.newDefaultIslandChain(testcase);
        int i;
        for (i = 0; i < startingNumberOfTowersPerPlayer.get(tested.accessTeams().size()); i++) {
            tested.changeIslandTowers(i, TowerEnum.WHITE);
        }
        int finalInt = i;
        assertThrows(GameShouldBeOverException.class, ()-> tested.changeIslandTowers(finalInt, TowerEnum.WHITE));
    }
    @ParameterizedTest
    @CsvSource({"0, WHITE", "1, BLACK", "2, WHITE", "3, BLACK", "12, WHITE"})
    void changeIslandTowersShouldLeaveEveryThingTheSameIfChangingTheOldTowersWithThemselves(int island, TowerEnum colour) {
        IslandChain tested = standardTestCase();

        tested.changeIslandTowers(island, colour);
        Island previous = tested.accessIsland(island).copy();

        tested.changeIslandTowers(island, colour);

        assertEquals(previous, tested.accessIsland(island));
    }
    @ParameterizedTest
    @CsvSource({"0", "1", "2", "10", "11", "12", "13"})
    void changeIslandTowersShouldNotWorkWithTowersNobodyPossesses(int islandIndex) {
        IslandChain tested = twoPlayersStandardTestCase();
        Island previousIslandStatus = tested.accessIsland(islandIndex).copy();

        tested.changeIslandTowers(islandIndex, TowerEnum.GREY); // in a two players game nobody has the grey towers

        Island actualIslandStatus = tested.accessIsland(islandIndex).copy();
        assertEquals(previousIslandStatus, actualIslandStatus);
    }

    //TODO : changeIslandTest with islandsOfDifferentSizes
    //TODO : try triggering GameShouldBeOverException

    @ParameterizedTest
    @MethodSource("mergeIslandDefaultSuite")
    void mergeIslandShouldReduceNumberOfIslands(TowerEnum colour, int[] islandsToMerge) {
        IslandChain tested = standardTestCase();
        int previousNumberOfIslands = tested.numberOfIslands();
        for (Integer index : islandsToMerge) {
            tested.changeIslandTowers(index, colour);
        }
        tested.mergeIslands();
        assertEquals(previousNumberOfIslands - islandsToMerge.length + 1, tested.numberOfIslands());
    }
    @ParameterizedTest
    @MethodSource("mergeIslandDefaultSuite")
    void mergeIslandShouldIncreaseIslandSize(TowerEnum colour, int[] islandsToMerge) {
        IslandChain tested = standardTestCase();

        for (Integer index : islandsToMerge) {
            tested.changeIslandTowers(index, colour);
        }
        tested.mergeIslands();
        assertEquals(islandsToMerge.length, tested.accessIsland(islandsToMerge[0]).getSize());
    }
    @ParameterizedTest
    @MethodSource("mergeIslandEdgeSuite")
    void mergeIslandShouldWorkOnTheEdge(TowerEnum colour, int[] islandsToMerge) {
        IslandChain tested = standardTestCase();
        Island interestedIsland = tested.accessIsland(islandsToMerge[0]);
        for (Integer index : islandsToMerge) {
            tested.changeIslandTowers(index, colour);
        }
        tested.mergeIslands();

        assertEquals(islandsToMerge.length, interestedIsland.getSize());
        assertEquals(1, tested.accessIsland(0).getSize());
    }
    @ParameterizedTest
    @MethodSource("mergeIslandDefaultSuite")
   void mergeIslandShouldNeverThrowExceptions(TowerEnum colour, int[] islandsToMerge) {
        IslandChain tested = standardTestCase();
        for (Integer index : islandsToMerge) {
            tested.changeIslandTowers(index, colour);
        }

        assertDoesNotThrow(tested::mergeIslands);
    }

    @ParameterizedTest
    @CsvSource({"0, Republicans", "1, Democrats", "10, Commie", "12, Republicans"})
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldWorkOnTheCorrectIsland(int desiredMotherNaturePos, String winner) {
        IslandChain tested = standardTestCase();

        Optional<Team> winningTeam = tested.accessTeam(winner);
        assert(winningTeam.isPresent());
        TowerEnum expectedColour = winningTeam.get().getTowerColour();

        tested.moveMotherNature(desiredMotherNaturePos);
        tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);

        TowerEnum actualColour = tested.accessIsland(desiredMotherNaturePos).getTower();

        assertEquals(expectedColour, actualColour);
    }
    @ParameterizedTest
    @CsvSource({"0, Republicans", "1, Democrats", "10, Commie", "12, Republicans"})
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldFavorTempMNOverPresentOne(int motherNaturePos, String winner) {
        IslandChain tested = standardTestCase();

        Optional<Team> winningTeam = tested.accessTeam(winner);
        assert(winningTeam.isPresent());
        TowerEnum expectedColour = winningTeam.get().getTowerColour();

        tested.addTempMotherNature(motherNaturePos);
        tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);

        TowerEnum actualColour = tested.accessIsland(motherNaturePos).getTower();

        assertEquals(expectedColour, actualColour);
    }
    @ParameterizedTest
    @CsvSource({"0, Republicans", "1, Democrats", "10, Commie", "12, Republicans"})
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldPickTheRightTeam(int motherNaturePos, String winner) {
        IslandChain tested = standardTestCase();

        Optional<Team> expectedWinnerTeam = tested.accessTeam(winner);


        tested.moveMotherNature(motherNaturePos);
        tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);


        Optional<Team> actualWinnerTeam = tested
                .ownerOfTowers(tested.accessIsland(motherNaturePos).copy().getTower());


        assertEquals(expectedWinnerTeam, actualWinnerTeam);
    }

    @ParameterizedTest
    @CsvSource({"Republicans, Democrats", "Democrats, Republicans", "Democrats, Commie"})
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldReturnTheCorrectNumberOfTowersToTheTeam(String firstWinner, String secondWinner) {
        IslandChain tested = standardTestCase();

        Optional<Team> firstWinningTeam = tested.accessTeam(firstWinner);
        assert(firstWinningTeam.isPresent());
        int firstTeamOriginalTowers = firstWinningTeam.get().getNumberOfTowers();

        tested.changeCurrentIslandTowersWithThoseOfTheWinner(firstWinner);
        tested.changeCurrentIslandTowersWithThoseOfTheWinner(secondWinner);

        firstWinningTeam = tested.accessTeam(firstWinner);
        assert firstWinningTeam.isPresent();
        int firstTeamNewTowers = firstWinningTeam.get().getNumberOfTowers();

        assertEquals(firstTeamOriginalTowers, firstTeamNewTowers);

    }
    @ParameterizedTest
    @MethodSource("defaultSuite")
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldFailWhenThePlayerNoLongerHasTowers(Map<String, List<String>> testcase) {
        IslandChain tested = IslandChain.newDefaultIslandChain(testcase);
        String winner = tested.accessTeams().get(0).getName(); //first team

        int i;
        for (i = 0; i < startingNumberOfTowersPerPlayer.get(tested.accessTeams().size()); i++) {
            tested.moveMotherNature(1);
            tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);
        }
        tested.moveMotherNature(1);
        assertThrows(GameShouldBeOverException.class, ()-> tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner));
    }
    @Test
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldDoNothingWhenTheSelectedPlayerDoesNotExist() {
        IslandChain tested = standardTestCase();
        tested.changeCurrentIslandTowersWithThoseOfTheWinner("Cazzimma Joe");
        assertEquals(standardTestCase(), tested); // tested should be unchanged
    }
    @ParameterizedTest
    @CsvSource({"0, Republicans", "1, Democrats", "10, Commie", "12, Republicans"})
    void changeCurrentIslandTowersWithThoseOfTheWinnerShouldLeaveEverythingUnchangedIfTheWinnersAreThePreviousOwners(int islandIndex, String winner) {
        IslandChain tested = standardTestCase();

        tested.moveMotherNature(islandIndex);
        tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);

        Island currentIslandStatusAfterFirstChange = tested.accessIsland(islandIndex).copy();

        tested.changeCurrentIslandTowersWithThoseOfTheWinner(winner);
        Island currentIslandStatusAfterSecondChange = tested.accessIsland(islandIndex).copy();

        assertEquals(currentIslandStatusAfterFirstChange, currentIslandStatusAfterSecondChange);
    }
    @Test
    void getTeamOfShouldFindTheCorrectTeam() {
        IslandChain tested = standardTestCase();
        for (Team team : tested.accessTeams()) {
            for (String player : team.getPlayers()) {
                assert (tested.accessTeamOf(player).isPresent());
                assertEquals(team, tested.accessTeamOf(player).get());
            }
        }
    }
    @ParameterizedTest
    @MethodSource("defaultSuite")
    void hashcodeTest(Map<String, List<String>> testcase) {
        IslandChain tested = IslandChain.newDefaultIslandChain(testcase);
        IslandChain secondTested = IslandChain.newDefaultIslandChain(testcase);
        assertEquals(tested.hashCode(), secondTested.hashCode());
    }
   @Test
    void simpleSerializedTest() {
    }

    public static IslandChain standardTestCase() {
        return IslandChain.newDefaultIslandChain(new HashMap<>() {{
            put("Republicans", List.of("Orange Man"));
            put("Democrats", List.of("Sleepy Joe"));
            put("Commie", List.of("Bernie Sanders"));
        }});
    }
    public static IslandChain twoPlayersStandardTestCase() {
        return IslandChain.newDefaultIslandChain(new HashMap<>() {{
            put("Republicans", List.of("Orange Man"));
            put("Democrats", List.of("Sleepy Joe"));
        }});
    }

    public static Stream<Arguments> defaultSuite() {
        return Stream.of(
                Arguments.of(new HashMap<>() {{
                    put("Millennial leftists", List.of("Intercontinental Ballistic Tweets"));
                    put("Cancel culture strikes again", List.of("Some Guy"));
                }}),
                Arguments.of(new HashMap<>() {{
                    put("Republicans", List.of("Orange Man"));
                    put("Democrats", List.of("Sleepy Joe"));
                    put("Commie", List.of("Bernie Sanders"));
                }}),
                Arguments.of(new HashMap<>() {{
                    put("Kuomintang", List.of("Generalissimo", "RiceBoi"));
                    put("CCP", List.of("Glorious Leader", "StarvingBoi"));
                }})
        );
    }

    public static Stream<Arguments> wrongTeamSizesSuite() {
        return Stream.of(
                Arguments.of(new HashMap<>() {{
                    put("Millennial leftists", List.of("Intercontinental Ballistic Tweets"));
                    put("Cancel culture strikes again", List.of());
                }}),
                Arguments.of(new HashMap<>() {{
                    put("Republicans", List.of("Orange Man", "Less orange man", "mandarin"));
                    put("Democrats", List.of("Sleepy Joe"));
                    put("Commie", List.of("Bernie Sanders"));
                }}),
                Arguments.of(new HashMap<>() {{
                    put("Kuomintang", List.of("Generalissimo", "RiceBoi", "Chinese Warlord"));
                    put("CCP", List.of("Glorious Leader", "StarvingBoi"));
                }}),
                Arguments.of(new HashMap<>() {{
                    put("Kuomintang", List.of("Generalissimo", "RiceBoi", "Chinese Warlord"));
                    put("CCP after the Long Walk", List.of());
                }})
        );
    }
    public static Stream<Arguments> sameTeamHomonymSuite(){
        return Stream.of(
                Arguments.of(new HashMap<>(){{
                   put("Democrats",List.of("PolioMan(FDR)", "PolioMan(FDR)"));
                   put("Republicans", List.of("Someone, Somebody"));
                }}),
                Arguments.of(new HashMap<>(){{
                    put("Democrats",List.of("Sleepy Joe", "Pamela Anderson"));
                    put("Republicans", List.of("OrangeMan", "OrangeMan"));
                }}),
                Arguments.of(new HashMap<>(){{
                    put("Democrats",List.of("PolioMan(FDR)", "PolioMan(FDR)"));
                    put("Republicans", List.of("OrangeMan", "OrangeMan"));
                }})
        );
        }
    public static Stream<Arguments> differentTeamHomonymSuite() {
        return Stream.of(
                Arguments.of(new HashMap<>(){{
                    put("Nigeria", List.of("United Kingdom", "Israel"));
                    put("Benin", List.of("France", "Israel"));
                }}),
                Arguments.of(new HashMap<>(){{
                    put("Nigeria", List.of("United Kingdom", "Israel"));
                    put("Benin", List.of("Israel", "France"));
                }}),
                Arguments.of(new HashMap<>(){{
                    put("Imperialist scum", List.of("United Kingdom", "France"));
                    put("Cool Kids", List.of("United Kingdom", "France"));
                }}),
                Arguments.of(new HashMap<>(){{
                    put("Imperialist scum", List.of("United Kingdom", "France"));
                    put("Cool Kids", List.of("France", "United Kingdom"));
                }})
        );
        }

    public static Stream<Arguments> addTokensSuite() {
        return Stream.of(
                Arguments.of(0, TokenCollection.createCollection(new HashMap<>() {{
                    put(TokenEnum.RED, 1);
                    put(TokenEnum.VIOLET, 0);
                    put(TokenEnum.GREEN, 4);
                }})),
                Arguments.of(1, TokenCollection.createCollection(new HashMap<>() {{
                    put(TokenEnum.GREEN, 1000);
                    put(TokenEnum.YELLOW, 4);
                    put(TokenEnum.VIOLET, 6);
                }})),
                Arguments.of(12, TokenCollection.createCollection(new HashMap<>() {{
                    put(TokenEnum.RED, 10);
                    put(TokenEnum.GREEN, 5);
                    put(TokenEnum.YELLOW, 0);
                }})),
                Arguments.of(7, TokenCollection.createCollection(new HashMap<>() {
                })),
                Arguments.of(5, TokenCollection.newEmptyCollection()),
                Arguments.of(11, TokenCollection.newDefaultBag())
        );
        }
    public static Stream<Arguments> mergeIslandDefaultSuite() {
        return Stream.of(
                Arguments.of(TowerEnum.WHITE, new int[]{0, 1}),
                Arguments.of(TowerEnum.WHITE, new int[]{0, 1}),
                Arguments.of(TowerEnum.WHITE, new int[]{0, 1, 2, 3, 4, 5}),
                Arguments.of(TowerEnum.WHITE, new int[]{0, 1, 2, 3, 4, 5})
        );
    }
    public static Stream<Arguments> mergeIslandEdgeSuite() {
        return Stream.of(
                Arguments.of(TowerEnum.WHITE, new int[]{11, 0}),
                Arguments.of(TowerEnum.WHITE, new int[]{11, 0, 1, 2}),
                Arguments.of(TowerEnum.WHITE, new int[]{8, 9, 10, 11, 12}),
                Arguments.of(TowerEnum.WHITE, new int[]{8, 9, 10, 11, 0}),
                Arguments.of(TowerEnum.WHITE, new int[]{11, 12, 1})
        );
    }

}
