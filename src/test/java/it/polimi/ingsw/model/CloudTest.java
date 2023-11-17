package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    @Test
    public void addTokensTest(){
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 2);
        coll.addTokens(TokenEnum.BLUE, 2);

        Cloud clo = new Cloud(3);
        assertThrows(StackOverflowError.class,
                ()->{

                    clo.addTokens(coll);
                }
        );
        Cloud clo1 = new Cloud(4);
        clo1.addTokens(coll);
        assert clo1.removeTokens().equals(coll);
    }

    @Test
    public void remoteCollectionTest(){
        TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 2);
        coll.addTokens(TokenEnum.BLUE, 2);
        Cloud clo = new Cloud(4);
        clo.addTokens(coll);
        clo.hashCode();
        assertTrue(clo.removeTokens().equals(coll));
        assertTrue(clo.removeTokens().equals(TokenCollection.newEmptyCollection()));
    }

    public void printTokens(TokenCollection tkC){
        System.out.println("\nBLUE: " + tkC.get(TokenEnum.BLUE));
        System.out.println("RED: " + tkC.get(TokenEnum.RED));
        System.out.println("YELLOW: " + tkC.get(TokenEnum.YELLOW));
        System.out.println("GREEN: " + tkC.get(TokenEnum.GREEN));
        System.out.println("VIOLET: " + tkC.get(TokenEnum.VIOLET));
    }
    @Test
    void serializable() {
        Cloud testCloud = new Cloud(4);
        Gson gson = CustomJsonBuilder.createDeserializer();
        System.out.println(gson.toJson(testCloud));
        List<Cloud> clouds = new ArrayList<>();
        clouds.add(new Cloud(4));
        clouds.add(new Cloud(4));
        clouds.add(new Cloud(4));
        clouds.add(new Cloud(4));
        System.out.println(gson.toJson(clouds));
    }

}