package it.polimi.ingsw.model.game_event;

import com.google.gson.Gson;
import it.polimi.ingsw.custom_json_builder.CustomJsonBuilder;
import it.polimi.ingsw.custom_json_builder.GsonablePrototype;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameEventTest {
    private final Gson serializer = CustomJsonBuilder.createSerializer();
    private final Gson deserializer = CustomJsonBuilder.createDeserializer();

    @ParameterizedTest
    @MethodSource("testCases")
    void serializationTest1(GameEvent tested) {
        String serialized = serializer.toJson(tested);
        System.out.println(serialized);
        assertEquals(tested, deserializer.fromJson(serialized, GameEvent.class));
    }
    @ParameterizedTest
    @MethodSource("testCases")
    void serializationShouldNotCrash(GameEvent tested) {
        assertDoesNotThrow(() -> serializer.toJson(tested));
    }
    @ParameterizedTest
    @MethodSource("testCases")
    void deserializationShouldNotCrash(GameEvent tested) {
        String serialized = serializer.toJson(tested);
        assertDoesNotThrow(() -> deserializer.fromJson(serialized, GameEvent.class));
    }
    @ParameterizedTest
    @MethodSource("testCases")
    void testNotNull(GameEvent tested) {
        assertDoesNotThrow(() -> tested.getClassName() + tested.hashCode());
    }

    public static Stream<Arguments> testCases() {
        return Stream.of(
          Arguments.of(new emptyTestGameEvent()),
          Arguments.of(new testGameEvent("hello"))
        );
    }



}

class testGameEvent extends GsonablePrototype implements GameEvent {
    private final String content;
    protected testGameEvent(String content) {
        super();
        this.content = content;
    }

    @Override
    public void visit(GameEventHandler eventHandler) {
        System.out.println("content is: " + content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        testGameEvent that = (testGameEvent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "testGameEvent{" +
                "content='" + content + '\'' +
                '}';
    }
}

class emptyTestGameEvent extends GsonablePrototype implements GameEvent {
    @Override
    public void visit(GameEventHandler eventHandler) {
        System.out.println("kys");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}