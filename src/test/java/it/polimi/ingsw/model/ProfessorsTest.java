package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorsTest {

    private Professors testProfessors;
    private List<String> playersID;
    @BeforeEach
    public void reset(){
        this.testProfessors = new Professors();
        this.playersID = List.of("A", "B", "C");

    }
    private void fillProfessors(){
        Map<String, TokenCollection> input = new HashMap<>();
        Random randint = new Random();


        this.playersID
                .forEach(player->input.put(player,
                        TokenCollection.createCollection(
                                new HashMap<TokenEnum, Integer>(
                                        Stream.of(TokenEnum.values()).map(token->Map.entry(token, randint.nextInt(10)))
                                                .collect(Collectors.toMap(Entry::getKey, Entry::getValue)))

                        )));
        System.out.println(input);

        //this.testProfessors.updateProfessorStatus();
    }
    @Test
    void correctConstructor(){
        assertNull(testProfessors.playerWithBonus);
        //check all values are null
        assertNull(testProfessors.owners.values().stream().reduce(null, (sub, value)->(Objects.isNull(value))?null:value));
    }
    @Test
    void getOwners() {
        assertEquals(testProfessors.getOwners(), testProfessors.owners);
        assertNotSame(testProfessors.getOwners(), testProfessors.owners);
        fillProfessors();
        assertEquals(testProfessors.getOwners(), testProfessors.owners);
        assertNotSame(testProfessors.getOwners(), testProfessors.owners);
    }

    @Test
    void getOwner() {
        TokenEnum token = TokenEnum.random();
        assertEquals(testProfessors.getOwner(token), testProfessors.owners.get(token));
        //same because both null
        assertSame(testProfessors.getOwner(token), testProfessors.owners.get(token));

        fillProfessors();
        assertEquals(testProfessors.getOwner(token), testProfessors.owners.get(token));
    }

    @Test
    void copy() {
        assertEquals(testProfessors, testProfessors.copy());
        assertNotSame(testProfessors, testProfessors.copy());
        fillProfessors();
        assertEquals(testProfessors, testProfessors.copy());
        assertNotSame(testProfessors, testProfessors.copy());
    }

    @Test
    void prepareForNextTurn() {
        testProfessors.prepareForNextRound();
        assertNull(testProfessors.playerWithBonus);
        fillProfessors();
        testProfessors.prepareForNextRound();
        assertNull(testProfessors.playerWithBonus);
    }

    @Test
    void setPlayerWithBonus() {
        testProfessors.setPlayerWithBonus("test");
        assertEquals("test", testProfessors.playerWithBonus);
        fillProfessors();
        testProfessors.setPlayerWithBonus("test");
        assertEquals("test", testProfessors.playerWithBonus);
    }

    @Test
    void updateProfessorStatus() {
        Professors empty = testProfessors.copy();
        fillProfessors();
        Professors p2 = testProfessors.copy();
        p2.setPlayerWithBonus("A");
        fillProfessors();
        Professors p3 = testProfessors.copy();
        assertNotSame(empty,p2);
        assertNotEquals(empty,p2);
        assertNotSame(p2,p3);
        assertNotEquals(p2,p3);
        assertEquals(empty.hashCode(), empty.hashCode());

        //works default
        Map<String, TokenCollection> input = playersID.stream().map(playerID->Map.entry(playerID, TokenCollection.newDefaultBag())).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        p2 = empty.copy();
        p2.updateProfessorStatus(input);
        assertNotEquals(empty,p2);
        assertTrue(p2.owners.values().stream().allMatch(i->i.equals("A")));

        //works with bonus
        p2.setPlayerWithBonus("B");
        p2.updateProfessorStatus(input);
        assertTrue(p2.owners.values().stream().allMatch(i->i.equals("B")));

    }


    @Test
    void json(){
        Gson gson = CustomJsonBuilder.createDeserializer();
        assertEquals(testProfessors, gson.fromJson(gson.toJson(testProfessors), Professors.class));
    }
}