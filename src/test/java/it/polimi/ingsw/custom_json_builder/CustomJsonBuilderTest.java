package it.polimi.ingsw.custom_json_builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomJsonBuilderTest {
    @Test
    void privateConstructor(){
        assertThrows(IllegalStateException.class, CustomJsonBuilder::new);
    }

}