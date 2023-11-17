package it.polimi.ingsw.model.cardhandler;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class ApprenticeCard {
    Integer cardID;
    Integer orderValue;
    Integer movementValue;

    ApprenticeCard(@NotNull Integer cardID, @NotNull Integer orderValue, @NotNull Integer movementValue) {
        this.cardID = cardID;
        this.orderValue = orderValue;
        this.movementValue = movementValue;
    }

    public Integer getMovementValue() {
        return movementValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApprenticeCard that = (ApprenticeCard) o;
        return Objects.equals(cardID, that.cardID) && Objects.equals(orderValue, that.orderValue) && Objects.equals(movementValue, that.movementValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardID, orderValue, movementValue);
    }

    @Override
    public String toString() {
        return "ApprenticeCard{" +
                "cardID=" + cardID +
                ", orderValue=" + orderValue +
                ", movementValue=" + movementValue +
                '}';
    }
}
