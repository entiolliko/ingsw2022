package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.islands.exceptions.ConflictingTowersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    @Test
    void newEmptyIslandShouldHaveZeroTokensInIt() {
        assertTrue(Island.newEmptyIsland().getTokens().isEmpty());
    }
    @Test
    void newEmptyIslandShouldNotHaveMotherNatureInIt() {
        assertEquals(MotherNatureEnum.ABSENT, Island.newEmptyIsland().getMotherNatureStatus());
    }
    @Test
    void newEmptyIslandShouldHaveNoTowersInIt() {
        assertEquals(TowerEnum.NONE, Island.newEmptyIsland().getTower());
    }
    @Test
    void newEmptyIslandShouldHaveSizeOne() {
        assertEquals(1, Island.newEmptyIsland().getSize());
    }

    @DisplayName("getTokens should return a copy of tokens instead of the object per se, thus modifying one should leave the other unchanged")
    @Test
    void getTokensShouldReturnACopyInsteadOfItsOwnTokenCollection() {
        Island tested = Island.newEmptyIsland();
        TokenCollection gotTokens = tested.getTokens();

        assertEquals(gotTokens, tested.getTokens());
        assertNotSame(gotTokens, tested.getTokens());
    }

    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void injectShouldIncreaseByTheCorrectSize(List<Island> islands) {
        int previousSize = islands.get(0).getSize();
        islands.get(0).inject(islands.get(1));
        assertEquals(previousSize + islands.get(1).getSize(), islands.get(0).getSize());
    }
    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void injectShouldMaintainPreviousTowerColour(List<Island> islands) {
        TowerEnum previousTower = islands.get(0).getTower();
        islands.get(0).inject(islands.get(1));
        assertEquals(previousTower, islands.get(0).getTower());
    }
    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void injectShouldCorrectlyStoreMotherNature(List<Island> islands) {
        MotherNatureEnum expected = MotherNatureEnum.max(islands.get(0).getMotherNatureStatus(), islands.get(1).getMotherNatureStatus());
        islands.get(0).inject(islands.get(1));
        assertEquals(expected, islands.get(0).getMotherNatureStatus());
    }
    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void injectShouldCorrectlyMergeTheTokenCollections(List<Island> islands) {
        TokenCollection original = islands.get(0).getTokens();
        islands.get(0).inject(islands.get(1));
        original.addToCollection(islands.get(1).getTokens());
        assertEquals(original, islands.get(0).getTokens());

    }
    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void injectShouldBeCommutative(List<Island> islands) {
        Island copy1 = islands.get(0).copy(),
                copy2 = islands.get(1).copy();
        islands.get(0).inject(islands.get(1));
        copy2.inject(copy1);
        assertEquals(islands.get(0), copy2);
    }
    @ParameterizedTest
    @MethodSource("conflictingTowersSuite")
    void injectShouldNotAcceptIslandsWithDifferentTowerColours(List<Island> badIslands) {
        Island input_test = badIslands.get(1);
        Island test = badIslands.get(0);
        assertThrows(ConflictingTowersException.class, () -> test.inject(input_test));
    }

    @ParameterizedTest
    @MethodSource("injectTestsSuite")
    void addTokensShouldIncreaseNumberOfTokensCorrectly(List<Island> islands) {
        TokenCollection firstIslandCollection = islands.get(0).getTokens();
        firstIslandCollection.addToCollection(islands.get(1).getTokens());

        islands.get(0).addTokens(islands.get(1).getTokens());

        assertEquals(firstIslandCollection, islands.get(0).getTokens());
    }


    public static Stream<Arguments> injectTestsSuite() {
        List<List<Island>> returnValue = new ArrayList<>();
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.NONE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(0, TowerEnum.NONE, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(1, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(0, TowerEnum.BLACK, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.WHITE, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag()),
                Island.createIsland(1, TowerEnum.WHITE, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.GREY, MotherNatureEnum.PRESENT, TokenCollection.newEmptyCollection()),
                Island.createIsland(0, TowerEnum.GREY, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(12, TowerEnum.NONE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(13, TowerEnum.NONE, MotherNatureEnum.ABSENT, TokenCollection.newEmptyCollection())
        ));

        return returnValue.stream().map(Arguments::of);
    }

    public static Stream<Arguments> conflictingTowersSuite() {
        List<List<Island>> returnValue = new ArrayList<>();
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.NONE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(0, TowerEnum.BLACK, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(1, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(0, TowerEnum.WHITE, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.BLACK, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag()),
                Island.createIsland(1, TowerEnum.GREY, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(0, TowerEnum.WHITE, MotherNatureEnum.PRESENT, TokenCollection.newEmptyCollection()),
                Island.createIsland(0, TowerEnum.GREY, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag())
        ));
        returnValue.add(List.of(
                Island.createIsland(12, TowerEnum.WHITE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()),
                Island.createIsland(13, TowerEnum.BLACK, MotherNatureEnum.ABSENT, TokenCollection.newEmptyCollection())
        ));

        return returnValue.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @CsvSource({"PRESENT", "ABSENT", "TEMP"})
    void setMotherNatureStatusShouldWork(MotherNatureEnum expected) {
        Island tested = Island.newEmptyIsland();
        tested.setMotherNatureStatus(expected);
        assertEquals(expected, tested.getMotherNatureStatus());
    }
    @ParameterizedTest
    @CsvSource({"NONE", "WHITE", "BLACK", "GREY"})
    void changeTowerColourShouldWork(TowerEnum expected) {
        Island tested = Island.newEmptyIsland();
        tested.changeTowerColour(expected);
        assertEquals(expected, tested.getTower());
    }

    @ParameterizedTest
    @MethodSource("towerEnumNoneSuite")
    void isMergeableWithShouldFailWithTowerEnumNone(Island testedA, Island testedB) {
        assertFalse(testedA.isMergeableWith(testedB));
    }
    @ParameterizedTest
    @MethodSource("differentTowerEnumSuite")
    void isMergeableWithShouldFailWithDifferentColour(Island testedA, Island testedB) {
        assertFalse(testedA.isMergeableWith(testedB));
    }
    @ParameterizedTest
    @MethodSource("sameTowerNonNoneSuite")
    void isMergeableWithShouldAcceptTowersOfTheSameColourThatAreNotTowerEnumNone(Island testedA, Island testedB) {
        assertTrue(testedA.isMergeableWith(testedB));
    }
    public static Stream<Arguments> towerEnumNoneSuite() {
        return Stream.of(
                Arguments.of(Island.newEmptyIsland(), Island.newEmptyIsland()),
                Arguments.of(Island.newEmptyIsland(), Island.createIsland(1, TowerEnum.BLACK,MotherNatureEnum.ABSENT, TokenCollection.newEmptyCollection())),
                Arguments.of(Island.createIsland(7, TowerEnum.GREY, MotherNatureEnum.ABSENT, TokenCollection.newDefaultBag()), Island.newEmptyIsland()),
                Arguments.of(Island.createIsland(4, TowerEnum.GREY, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.NONE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()))
        );
    }
    public static Stream<Arguments> differentTowerEnumSuite() {
        return Stream.of(
                Arguments.of(Island.createIsland(4, TowerEnum.GREY, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.WHITE, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.WHITE, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.GREY, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.GREY, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()))
        );
    }
    public static Stream<Arguments> sameTowerNonNoneSuite() {
        return Stream.of(
                Arguments.of(Island.createIsland(4, TowerEnum.GREY, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.GREY, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.WHITE, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.WHITE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.WHITE, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.WHITE, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag())),
                Arguments.of(Island.createIsland(4, TowerEnum.BLACK, MotherNatureEnum.TEMP, TokenCollection.newEmptyCollection()), Island.createIsland(5, TowerEnum.BLACK, MotherNatureEnum.PRESENT, TokenCollection.newDefaultBag()))
        );
    }
}
