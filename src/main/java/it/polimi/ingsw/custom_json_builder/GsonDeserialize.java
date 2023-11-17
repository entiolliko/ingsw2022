package it.polimi.ingsw.custom_json_builder;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonDeserialize<T extends Gsonable> implements JsonDeserializer<T> {

    private static final String CLASSNAME = "classname";

    /**
     * creates a deserializer for the interface Gsonable that correctly instantiate the needed object saved in the attribute classname of the JsonElement
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param deserializationContext
     * @return an instance of the class pointed by the interface
     * @throws JsonParseException
     */
    public T deserialize(final JsonElement jsonElement, final Type type,
                         final JsonDeserializationContext deserializationContext
    ) throws JsonParseException {

        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        final String className = prim.getAsString();
        final Class<T> clazz = getClassInstance(className);
        return deserializationContext.deserialize(jsonObject, clazz);
    }

    /**
     *  return an instance of the specified class by String
     * @param className a String with the class name
     * @return an instance of the class
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClassInstance(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException(cnfe.getMessage());
        }
    }

}
