package it.polimi.ingsw.model.character_cards;

import com.google.gson.*;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.custom_json_builder.Gsonable;
import it.polimi.ingsw.model.custom_data_structures.MagicList;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import org.junit.jupiter.api.Test;

import javax.naming.directory.InvalidAttributesException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {

    @Test
    void serialize() throws InvalidAttributesException {

        Gson gson = CustomJsonBuilder.createDeserializer();
        CharacterCard tested = new Card1(TokenCollection.newDefaultBag().randomPop(4));
        String res = CustomJsonBuilder.createDeserializer().toJson(tested);
        System.out.println(res);
        assertEquals(tested, gson.fromJson(res, tested.getClass()));
        assertEquals(tested, gson.fromJson(res, CharacterCard.class));
        System.out.println(gson.fromJson(res,CharacterCard.class).getClassName());

        List<CharacterCard> cards = new MagicList();
        cards.add(new Card2());
        cards.add(new Card6());
        cards.add(new Card9());
        System.out.println(gson.fromJson(CustomJsonBuilder.createSerializer().toJson(cards),MagicList.class).stream().map(x -> x.getClass()).toList());
        assertEquals(cards, gson.fromJson(CustomJsonBuilder.createSerializer().toJson(cards),MagicList.class));

    }
}