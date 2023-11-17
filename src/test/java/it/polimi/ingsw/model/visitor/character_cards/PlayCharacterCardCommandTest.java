package it.polimi.ingsw.model.visitor.character_cards;


import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.controller.exceptions.InvalidCommandException;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.character_cards.*;
import it.polimi.ingsw.model.custom_data_structures.MagicList;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.TypeOfGame;
import it.polimi.ingsw.model.custom_data_structures.exceptions.ChoosingCardException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.MissingProfessorException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.PositiveAmountException;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.visitor.base_commands.PrepareForNextTurn;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayCharacterCardCommandTest {

    List<String> playersID;
    List<String> magicians;
    List<String> teams;
    String gameID;
    Game game;
    Gson serializer;
    Gson deserializer;

    @BeforeEach
    void ResetTest(){
        playersID = List.of("A","B");
        magicians = List.of("mA","mB");
        teams = List.of("A","n");
        gameID = "123.A)£$41";
        game = new Game(playersID, magicians, teams, gameID, TypeOfGame.EXPERT);
        serializer = CustomJsonBuilder.createSerializer();
        deserializer = CustomJsonBuilder.createDeserializer();
    }

    @Test
    void randomTest(){
        ChoosingCardException temp = new ChoosingCardException("");
        MissingProfessorException temp1 = new MissingProfessorException();
        temp1.getMessage();
        MagicList temp2 = new MagicList();
        temp2.getClassName();
        InvalidCommandException temp5 = new InvalidCommandException();
        temp5.toString("");
        TokenCollection temp3 = TokenCollection.newDefaultBag();
        TokenCollection coll = TokenCollection.newEmptyCollection();
        temp3.removeFromCollection(coll);

        PositiveAmountException temp6 = new PositiveAmountException("as");
        temp6.getMessage();
    }

    @Test
    void testCard1() throws ReloadGameException, InvalidAttributesException {
        final TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 3);
        coll.addTokens(TokenEnum.RED, 2);
        coll.addTokens(TokenEnum.YELLOW, 2);
        assertThrows(InvalidAttributesException.class, ()->new Card1(coll));
        TokenCollection coll1 = TokenCollection.newEmptyCollection();
        coll1.addTokens(TokenEnum.GREEN, 3);
        coll1.addTokens(TokenEnum.YELLOW, 1);
        Card1 testCard = new Card1(coll1);


        assertThrows(InvalidCommandException.class, () -> testCard.playCard(game.getGameBoard(), "A", TokenEnum.RED, -1, null, TokenCollection.newEmptyCollection()));
        int numOfGinBag = game.getGameBoard().getBag().size();
        int numOfGTB = game.getGameBoard().getIslands().getIslandTokens(1).size();
        testCard.reset(game.getGameBoard());
        game.getGameBoard().getDashBoards().get("A").addCoins(4);
        testCard.playCard(game.getGameBoard(), "A", TokenEnum.GREEN, 1, null, null);
        assertThrows(ReloadGameException.class, () -> testCard.playCard(game.getGameBoard(), "A", null, -1, TokenCollection.newDefaultBag(), null));

        assertEquals(game.getGameBoard().getIslands().getIslandTokens(1).size(), numOfGTB + 1);
        assertEquals(game.getGameBoard().getBag().size(), numOfGinBag - 1);
    }

    @Test
    void testCard2(){
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        List<CharacterCard> cardsList = game.getGameBoard().getCharacterCardList();

        try {
            cardsList.remove(0);
            cardsList.add(new Card2());
        }catch (Exception e){}

        try{assertEquals(new Card2(), cardsList.get(2));}catch (Exception e){}

        PlayCharacterCardCommand command = new PlayCharacterCardCommand("Card2", "A", null, -1,  null, null);
        command.getPlayerID();
        (new Card2()).getClassName();
        try{game.accept(command);}catch (Exception e){}

        PlayCharacterCardCommand command1 = new PlayCharacterCardCommand("Card2", "A", TokenEnum.GREEN, 1,  TokenCollection.newDefaultBag(), null);
        assertThrows(ReloadGameException.class, ()-> game.accept(command1));
    }

    @Test
    void testCard4(){
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        List<CharacterCard> cardsList = game.getGameBoard().getCharacterCardList();

        try {
            cardsList.remove(0);
            cardsList.add(new Card4());
        }catch (Exception e){}

        try{assertEquals(new Card4(), cardsList.get(2));}catch (Exception e){}

        PlayCharacterCardCommand command = new PlayCharacterCardCommand("Card4", "A", null, -1,  null, null);

        try{game.accept(command);}catch (Exception e){}

        command.getPlayerID();
        (new Card4()).getClassName();
        (new Card4()).toString();
        try{game.accept(command);}catch (Exception e){}

        PlayCharacterCardCommand command1 = new PlayCharacterCardCommand("Card4", "A", TokenEnum.GREEN, 1,  TokenCollection.newDefaultBag(), null);
        assertThrows(ReloadGameException.class, ()-> game.accept(command1));

    }

    @Test
    void testCard6(){
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        List<CharacterCard> cardsList = game.getGameBoard().getCharacterCardList();

        try {
            cardsList.remove(0);
            cardsList.add(new Card6());
        }catch (Exception e){}

        try{assertEquals(new Card6(), cardsList.get(2));}catch (Exception e){}

        PlayCharacterCardCommand command = new PlayCharacterCardCommand("Card6", "A", null, -1,  null, null);

        try{game.accept(command);}catch (Exception e){}

        command.getPlayerID();
        (new Card6()).getClassName();
        try{game.accept(command);}catch (Exception e){}

        PlayCharacterCardCommand command1 = new PlayCharacterCardCommand("Card6", "A", TokenEnum.GREEN, 1,  TokenCollection.newDefaultBag(), null);
        assertThrows(ReloadGameException.class, ()-> game.accept(command1));
    }

    @Test
    void testCard7() throws InvalidAttributesException {
        final TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 3);
        coll.addTokens(TokenEnum.RED, 2);
        coll.addTokens(TokenEnum.YELLOW, 2);
        assertThrows(InvalidAttributesException.class, ()->new Card7(coll));

        TokenCollection coll1 = TokenCollection.newEmptyCollection();
        coll1.addTokens(TokenEnum.GREEN, 3);
        coll1.addTokens(TokenEnum.YELLOW, 1);
        coll1.addTokens(TokenEnum.RED, 2);
        Card7 testCard = new Card7(coll1);

        testCard.getClassName();

        assertThrows(InvalidCommandException.class, () -> testCard.playCard(game.getGameBoard(), "A", TokenEnum.RED, -1, null, null));
        assertThrows(ReloadGameException.class, () -> testCard.playCard(game.getGameBoard(), "A", null, -1, TokenCollection.newDefaultBag(), null));

        TokenCollection toRemove = TokenCollection.newEmptyCollection();
        TokenCollection toAdd = TokenCollection.newEmptyCollection();


        int numOfGinBag = game.getGameBoard().getBag().size();

        toRemove.addToCollection(game.getGameBoard().getDashBoards().get("A").cloneEntranceHall().randomPop(1));

        int numOfGTB;
        if(toRemove.get(TokenEnum.GREEN) == 1)
            numOfGTB = game.getGameBoard().getDashBoards().get("A").cloneEntranceHall().get(TokenEnum.GREEN) - 1;
        else
            numOfGTB = game.getGameBoard().getDashBoards().get("A").cloneEntranceHall().get(TokenEnum.GREEN);

        toAdd.addTokens(TokenEnum.GREEN, 1);

        game.getGameBoard().getDashBoards().get("A").addCoins(4);

        try {
            testCard.playCard(game.getGameBoard(), "A", null, -1, toAdd, toRemove);
        } catch (ReloadGameException e) {


        }

        assertEquals(game.getGameBoard().getDashBoards().get("A").cloneEntranceHall().get(TokenEnum.GREEN), numOfGTB + 1);
        assertEquals(game.getGameBoard().getBag().size(), numOfGinBag);
    }

    @Test
    void testCard8(){
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        List<CharacterCard> cardsList = game.getGameBoard().getCharacterCardList();

        try {
            cardsList.remove(0);
            cardsList.add(new Card8());
        }catch (Exception e){}

        try{assertEquals(new Card8(), cardsList.get(2));}catch (Exception e){}

        PlayCharacterCardCommand command = new PlayCharacterCardCommand("Card8", "A", null, -1,  null, null);

        try{game.accept(command);}catch (Exception e){}

        command.getPlayerID();
        (new Card8()).getClassName();
        try{game.accept(command);}catch (Exception e){}

        PlayCharacterCardCommand command1 = new PlayCharacterCardCommand("Card8", "A", TokenEnum.GREEN, 1,  TokenCollection.newDefaultBag(), null);
        assertThrows(ReloadGameException.class, ()-> game.accept(command1));
    }

    @Test
    void testCard9(){
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        List<CharacterCard> cardsList = game.getGameBoard().getCharacterCardList();

        try {
            cardsList.remove(0);
            cardsList.add(new Card9());
        }catch (Exception e){}

        try{assertEquals(new Card9(), cardsList.get(2));}catch (Exception e){}

        PlayCharacterCardCommand command = new PlayCharacterCardCommand("Card9", "A", null, -1,  null, null);

        try{game.accept(command);}catch (Exception e){}

        command.getPlayerID();
        (new Card9()).getClassName();
        try{game.accept(command);}catch (Exception e){}

        PlayCharacterCardCommand command1 = new PlayCharacterCardCommand("Card9", "A", TokenEnum.GREEN, 1,  TokenCollection.newDefaultBag(), null);
        assertThrows(ReloadGameException.class, ()-> game.accept(command1));
    }

    @Test
    void testCard11() throws ReloadGameException, InvalidAttributesException {
        game.getGameBoard().getDashBoards().get("A").addCoins(8);
        final TokenCollection coll = TokenCollection.newEmptyCollection();
        coll.addTokens(TokenEnum.GREEN, 2);
        coll.addTokens(TokenEnum.RED, 2);
        coll.addTokens(TokenEnum.YELLOW, 2);
        assertThrows(InvalidAttributesException.class, ()->new Card11(coll));

        TokenCollection coll1 = TokenCollection.newEmptyCollection();
        coll1.addTokens(TokenEnum.GREEN, 2);
        coll1.addTokens(TokenEnum.YELLOW, 1);
        coll1.addTokens(TokenEnum.RED, 1);
        Card11 testCard = new Card11(coll1);

        testCard.getClassName();
        System.out.println(testCard);

        assertThrows(InvalidCommandException.class, () -> testCard.playCard(game.getGameBoard(), "A", null, -1, null, null));
        assertThrows(ReloadGameException.class, () -> testCard.playCard(game.getGameBoard(), "A", null, -1, TokenCollection.newDefaultBag(), null));


        int numOfGTB = game.getGameBoard().getDashBoards().get("A").cloneStudyHall().get(TokenEnum.GREEN);
        int numOfGinBag = game.getGameBoard().getBag().size();

        game.accept(new PrepareForNextTurn());
        System.out.println(testCard);
        testCard.playCard(game.getGameBoard(), "A", TokenEnum.GREEN, -1, null, null);
        assertEquals(game.getGameBoard().getDashBoards().get("A").cloneStudyHall().get(TokenEnum.GREEN), numOfGTB + 1);
        assertEquals(game.getGameBoard().getBag().size(), numOfGinBag - 1);
    }

    @Test
    void serializable(){
        TokenCollection temp = TokenCollection.newEmptyCollection();
        temp.addTokens(TokenEnum.GREEN, 1);
        PlayerVisitorCommand testCommand = new PlayCharacterCardCommand("Card11", "A", null, -1, temp, temp);
        assertEquals(testCommand, deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class));
        assertEquals(testCommand.hashCode(), deserializer.fromJson(serializer.toJson(testCommand), PlayerVisitorCommand.class).hashCode());
    }
}