package it.polimi.ingsw.model.visitor.player_visitor_command;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveToStudyHallFromTheEntranceTest {
    private Game testGame;
    private List<String> playersID;
    private List<String> magicians;
    private List<String> squads;
    private String gameID;
    MoveToStudyHall testVisitor;
    TokenCollection coll;
    Gson serializer;
    Gson deserializer;
    PlayerVisitorCommand testCommand;


    @BeforeEach
    void reset(){
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        this.gameID = "game01";
        this.testGame = new Game(playersID, magicians, squads, gameID, TypeOfGame.NORMAL);
        this.serializer = CustomJsonBuilder.createSerializer();
        this.deserializer = CustomJsonBuilder.createDeserializer();
        this.testCommand = new MoveToStudyHall("A", TokenEnum.GREEN);

       // this.testVisitor = new MoveToStudyHallFromTheEntrance("A", randomPresentToken("A"));
    }

    private TokenEnum randomPresentToken(String player) {
        TokenCollection playerEntranceHall = testGame.getGameBoard().getDashBoards().get(player).cloneEntranceHall();
        Set<TokenEnum> possibleColours = new TreeSet<>();
        for (TokenEnum colour : TokenEnum.values()) {
            if (playerEntranceHall.get(colour) > 0)
                possibleColours.add(colour);
        }
        return TokenEnum.randomAmong(possibleColours);
    }

    @ParameterizedTest
    @CsvSource({"1", "1", "1", "1", "1", "1"})
    void testCommand(){
        String randomPlayer = this.playersID.get(new Random().nextInt(playersID.size()));
        TokenEnum testedColour =  randomPresentToken(randomPlayer);
        Board br = testGame.getGameBoard();

        try{
            this.testVisitor = new MoveToStudyHall(randomPlayer, testedColour);
            this.testGame.accept(testVisitor);}
        catch (Exception e){
            e.printStackTrace();
        }

        assertTrue(br.getDashBoards().get(randomPlayer).cloneStudyHall().size() == 1 && br.getDashBoards().get(randomPlayer).cloneStudyHall().get(testedColour) == 1);
    }
    @Test
    void serializable(){
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }

}