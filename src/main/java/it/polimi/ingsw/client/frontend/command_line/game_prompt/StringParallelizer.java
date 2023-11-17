package it.polimi.ingsw.client.frontend.command_line.game_prompt;

import java.util.List;

public class StringParallelizer {
    private StringParallelizer() {

    }

    public static String convertInParallel(List<String> strings) {
        List<String[]> splitStrings = strings
                .stream()
                .map(string -> string.split(String.format("%n")))
                .toList();
        return parallelFormatter(splitStrings);
    }

    private static String parallelFormatter(List<String[]> boardsReps) {
        StringBuilder returnValue = new StringBuilder();
        int maximumLength = findMaximumLength(boardsReps);
        for (int i = 0; i < maximumLength; i++) {
            for (String[] row : boardsReps) {
                returnValue.append(row[i]).append("\t");
            }
            returnValue.append(String.format("%n"));
        }
        return returnValue.toString();
    }

    private static int findMaximumLength(List<String[]> boardsReps) {
        return boardsReps.stream().mapToInt(array -> array.length)
                .max()
                .orElse(0)
                ;
    }
}
