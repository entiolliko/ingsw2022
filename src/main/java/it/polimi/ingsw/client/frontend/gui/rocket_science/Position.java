package it.polimi.ingsw.client.frontend.gui.rocket_science;

public record Position(double x, double y) {
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
