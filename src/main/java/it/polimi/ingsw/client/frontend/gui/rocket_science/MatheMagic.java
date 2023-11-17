package it.polimi.ingsw.client.frontend.gui.rocket_science;

public class MatheMagic {
    private static final double PI = Math.PI;
    private MatheMagic() {

    }
    public static Position islandPosition(Position center, double radius, int islandIndex, int numberOfIslands, double initialOffset) {
        double angle = initialOffset + 2 * PI * ((double) islandIndex / numberOfIslands);
        //for traversing the circle in a counterclockwise fashion
        double x = center.x() - radius * Math.cos(angle);
        // y-axis is inverted in javaFX
        double y = center.y() - radius * Math.sin(angle);
        return new Position(x, y);
    }

}
