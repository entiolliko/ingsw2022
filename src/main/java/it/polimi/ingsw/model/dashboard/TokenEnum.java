package it.polimi.ingsw.model.dashboard;

import it.polimi.ingsw.model.custom_data_structures.exceptions.EmptyException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public enum TokenEnum {
    GREEN, RED, YELLOW, VIOLET, BLUE;
    private static final Random RANDOM = new Random();

    public static List<TokenEnum> allBut(Set<TokenEnum> toRemove) {
        return Arrays.stream(TokenEnum
                        .values())
                .filter(x -> !toRemove.contains(x))
                .toList();
    }

    public static List<TokenEnum> allBut(TokenEnum... toRemove) {
        return allBut(Arrays.stream(toRemove).collect(Collectors.toSet()));
    }

    public static TokenEnum random() {
        return randomAmong(Set.of(TokenEnum.values()));

    }

    public static TokenEnum randomAmong(Set<TokenEnum> choices) throws EmptyException {
        if (choices.isEmpty()) throw new EmptyException();
        int randInt = RANDOM.nextInt(choices.size());
        return (TokenEnum) choices.toArray()[randInt];
    }
}
