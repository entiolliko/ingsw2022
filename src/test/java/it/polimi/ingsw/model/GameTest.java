package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.client.backend.DefaultConnector;
import it.polimi.ingsw.client.frontend.command_line.CLIScreen;
import it.polimi.ingsw.client.frontend.command_line.LineInterpreter;
import it.polimi.ingsw.client.frontend.command_line.game_prompt.GamePrompt;
import it.polimi.ingsw.client.frontend.command_line.game_prompt.PrettyStringFormer;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayApprenticeCard;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    List<String> playersID;
    List<String> magicians;
    List<String> squads;
    String gameID;
    Game testGame;
    @BeforeEach
    void reset() {
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game0";
        testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.EXPERT);
    }

    @Test
    void correctConstructor(){
        assertEquals(testGame.getGameID(), this.gameID);
    }

    @Test
    void testSetIsGoingToBeOver(){
        testGame.setIsGoingToBeOver();
        testGame.getClassName();
        assertTrue(testGame.isGoingToBeOver());
    }

    @Test
    void testGetLead() throws ReloadGameException {

        testGame.accept(new PlayApprenticeCard("A", 3));
        testGame.accept(new PlayApprenticeCard("B", 7));
        testGame.accept(new PlayApprenticeCard("C", 5));
        testGame.accept(new PlayApprenticeCard("D", 4));
        assertTrue( testGame.getLeads().contains("A"));
    }

    @Test
    void testGetOrderedPlayers() throws ReloadGameException {
        testGame.accept(new PlayApprenticeCard("A", 1));
        testGame.accept(new PlayApprenticeCard("B", 2));
        testGame.accept(new PlayApprenticeCard("C", 3));
        assertEquals(List.of("A","B","C"), testGame.getOrderedPlayers());
    }

    @Test
    void testGetClockwisePlayers() throws ReloadGameException {
        testGame.accept(new PlayApprenticeCard("A", 2));
        testGame.accept(new PlayApprenticeCard("B", 3));
        testGame.accept(new PlayApprenticeCard("C", 1));
        testGame.accept(new PlayApprenticeCard("D", 4));
        testGame.getGameBoard().getCardHandler().prepareForNextRound();
        assertEquals(List.of("C","D","A","B"), testGame.getPlayersClockwise());
    }

    @Test
    void json() throws IOException {
        Gson deserializer = CustomJsonBuilder.createDeserializer();
        Gson serializer = CustomJsonBuilder.createSerializer();

        String game = serializer.toJson(testGame);
        System.out.println(game);
        Game gayme = deserializer.fromJson(game, Game.class);
        System.out.println(gayme.getGameBoard().getProfessors());
        assertEquals(testGame, deserializer.fromJson(game, Game.class));
    }

    @ParameterizedTest
    @MethodSource("twoPlayerSampleGame")
    void getLeadWithNoEqualNumberOfTowers(Game game) {
    //TODO : complete test
        String actual = "".concat("hello");
        assertEquals("hello",actual );
    }
    @Test
    void testSimplified() throws ReloadGameException {
        System.out.println(testGame.getDTO());
        System.out.println("---------------");

        PlayerVisitorCommand command = new PlayApprenticeCard("A", 2);
        PlayerVisitorCommand command1 = new PlayApprenticeCard("A", 7);
        PlayerVisitorCommand command2 = new PlayApprenticeCard("A", 9);
        testGame.accept(command);
        testGame.getGameBoard().getCardHandler().prepareForNextRound();
        testGame.accept(command1);
        testGame.getGameBoard().getCardHandler().prepareForNextRound();
        testGame.accept(command2);
        testGame.getGameBoard().getCardHandler().prepareForNextRound();
        System.out.println(testGame.getDTO());
    }
    @Test
    void ehiAmigo() throws ReloadGameException {
        for (int i = 0; i < 20; i++) {
            reset();
            testSimplified();
        }
    }

    public static Stream<Arguments> twoPlayerSampleGame() {
        return Stream.of(
                Arguments.of( new Game(List.of("Sleepy Joe", "Orange Man"), List.of("millennial wizard", "confederate sorcerer"), List.of("Democrats", "Republicans"), "1234", TypeOfGame.EXPERT))
        );
    }

    @Test

    void dto(){
        GameDTO gdto = testGame.getDTO();

        System.out.println(gdto);
        GamePrompt prompt = new GamePrompt(new PrintWriter(System.out));
        assertThrows(NullPointerException.class, ()->prompt.prompt(gdto));

    }

}