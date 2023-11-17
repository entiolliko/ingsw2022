package it.polimi.ingsw.custom_json_builder;

import com.google.gson.*;
import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.model.visitor.player_visitor_command.PlayerVisitorCommand;
import it.polimi.ingsw.model.character_cards.CharacterCard;
import it.polimi.ingsw.model.custom_data_structures.MagicList;
import it.polimi.ingsw.model.game_event.GameEvent;
import it.polimi.ingsw.model.state.State;

import java.util.Iterator;

public class CustomJsonBuilder {
    CustomJsonBuilder() {
        throw new IllegalStateException("Cannot instantiate an utility class");
    }

    /**
     * create a Gson deserializer
     * @return Gson() instance with all the options needed
     */
    public static Gson createDeserializer() {

        GsonBuilder gsonBuilder = new GsonBuilder().enableComplexMapKeySerialization();


        JsonDeserializer<MagicList> deserializerList = (jsonElement, type, jsonSerializationContext) -> {
            JsonArray jsonList = (JsonArray) jsonElement;
            Iterator<JsonElement> jsons = jsonList.iterator();
            MagicList returnValue = new MagicList();

            GsonBuilder build = new GsonBuilder();
            build.registerTypeAdapter(CharacterCard.class, new GsonDeserialize<CharacterCard>());
            Gson gson = build.create();
            while (jsons.hasNext()) {
                JsonObject json = jsons.next().getAsJsonObject();
                returnValue.add(gson.fromJson(json, CharacterCard.class));
            }
            return returnValue;
        };


        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(CharacterCard.class, new GsonDeserialize<CharacterCard>());
        gsonBuilder.registerTypeAdapter(GameEvent.class, new GsonDeserialize<GameEvent>());
        gsonBuilder.registerTypeAdapter(PlayerVisitorCommand.class, new GsonDeserialize<PlayerVisitorCommand>());
        gsonBuilder.registerTypeAdapter(State.class, new GsonDeserialize<State>());
        gsonBuilder.registerTypeAdapter(MagicList.class, deserializerList);
        gsonBuilder.registerTypeAdapter(ClientRequest.class, new GsonDeserialize<ClientRequest>());
        gsonBuilder.registerTypeAdapter(ServerResponse.class, new GsonDeserialize<ServerResponse>());
        return gsonBuilder.create();
    }

    /**
     * creates a custom Gson serializer
     * @return a custom Gson() instance
     */
    public static Gson createSerializer() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        return builder.create();
    }
}
