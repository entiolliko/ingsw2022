package it.polimi.ingsw.model.character_cards;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.Test;

import javax.naming.directory.InvalidAttributesException;
import static org.junit.jupiter.api.Assertions.*;

class Card1Test {

    @Test
    void serializable() throws InvalidAttributesException {
        Gson gson = CustomJsonBuilder.createDeserializer();
        TokenCollection tokens = TokenCollection.newEmptyCollection();
        tokens.addTokens(TokenEnum.GREEN, 2);
        tokens.addTokens(TokenEnum.YELLOW, 2);
        System.out.println(tokens);
        CharacterCard card = new Card1(tokens);
        System.out.println(gson.toJson(card));
        assertEquals(card, gson.fromJson(gson.toJson(card), card.getClass()));
        assertEquals(card, gson.fromJson(gson.toJson(card), CharacterCard.class));
        assertEquals(card.hashCode(),gson.fromJson(gson.toJson(card), card.getClass()).hashCode());
    }

}