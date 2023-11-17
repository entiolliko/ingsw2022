package it.polimi.ingsw.client.frontend.gui.rocket_science;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatheMagicTest {
    private static final double MARGIN_OF_ERROR = 0.01;
    @Test
    void testPos() {
        Position tested = MatheMagic.islandPosition(new Position(4, 3), 0.8, 5, 7, Math.PI / 2);
        Position expected = new Position(3.22, 3.178);
        assertTrue(assertClose(expected, tested));
    }

    private boolean assertClose(Position expected, Position tested) {
        System.out.println("expected: " + expected + " vs " + tested);
        System.out.println("differences are " + (expected.x() - tested.x()) + " for x and " + (expected.y() - tested.y()) + " for y");
        return Math.abs(expected.x() - tested.x()) < MARGIN_OF_ERROR && Math.abs(expected.y() - tested.y()) < MARGIN_OF_ERROR;
    }

}