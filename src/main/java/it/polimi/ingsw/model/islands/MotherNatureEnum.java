package it.polimi.ingsw.model.islands;

public enum MotherNatureEnum {
    PRESENT(2), ABSENT(0), TEMP(1);

    private final int presence;

    MotherNatureEnum(int presence) {
        this.presence = presence;
    }

    public static MotherNatureEnum max(MotherNatureEnum a, MotherNatureEnum b) {
        return a.presence >= b.presence ? a : b;
    }

}
