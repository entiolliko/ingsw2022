package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.character_cards.CharacterCard;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    List<String> playersID;
    List<String> magicians;
    List<String> squads;
    Board testBoard;
    Gson gson;
    @BeforeEach
    void reset(){
        this.playersID = List.of("A", "B", "C", "D");
        this.magicians = List.of("mA", "mB", "mC", "mD");
        this.squads = List.of("sA", "sA", "sC", "sD");
        testBoard = new Board(playersID, magicians, squads, false);
        gson = CustomJsonBuilder.createDeserializer();
    }


    @Test
    void correctConstructor() {
        List<CharacterCard> list = testBoard.getCharacterCardList();
        System.out.println(list.stream().map(x -> x.getClass().getSimpleName()).toList());
        for (int i = 0; i < testBoard.getPlayersID().size(); i++) {
            assertEquals(7, testBoard.getDashBoards().get(testBoard.getPlayersID().get(i)).cloneEntranceHall().size());
            assertEquals(testBoard.getDashBoards().get(testBoard.getPlayersID().get(i)).cloneStudyHall().size(), 0);
            assertEquals(testBoard.getClouds().get(i).size(), 3);
        }

        for (int i = 0; i < 12; i++) {
            if (testBoard.getIslands().getCurrMotherNaturePos() != i && i != ((testBoard.getIslands().getCurrMotherNaturePos() + 6) % 12))
                assertEquals(testBoard.getIslands().accessIsland(i).getTokens().size(), 1);
            else
                assertEquals(testBoard.getIslands().accessIsland(i).getTokens().size(), 0);
        }

        for(TokenEnum i : TokenEnum.values())
            assertEquals(null, testBoard.getProfessors().getOwners().get(i));
        System.out.println(testBoard.getCardHandler().getFirstPlayerToMove());
    }

    @Test
    void equals() {
        assertNotSame(testBoard, new Board(playersID, magicians, squads, false));
    }

    @Test
    void serializable(){
      Gson serializer = CustomJsonBuilder.createSerializer();
      Gson deserializer = CustomJsonBuilder.createDeserializer();
      Board afterSerialization = deserializer.fromJson(serializer.toJson(testBoard), Board.class);
      assertEquals(testBoard, afterSerialization);

    }

}