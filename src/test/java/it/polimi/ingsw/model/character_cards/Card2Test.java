package it.polimi.ingsw.model.character_cards;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Card2Test {
    @Test
    void serializable(){
        Gson gson = CustomJsonBuilder.createDeserializer();
        CharacterCard card = new Card2();
        System.out.println(gson.toJson(card));
        assertEquals(card, gson.fromJson(gson.toJson(card), card.getClass()));
        assertEquals(card, gson.fromJson(gson.toJson(card), CharacterCard.class));
        assertEquals(card.hashCode(),gson.fromJson(gson.toJson(card), card.getClass()).hashCode());
    }

}