package it.polimi.ingsw.client.frontend.command_line.game_prompt;

import it.polimi.ingsw.controller.data_transfer_objects.CharacterCardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.DashboardDTO;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.IslandDTO;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrettyStringFormer {
    private String yourName;

    public static String supremeLogo() {
        return """


                                                  ..:~!7JYY55PPPPGGGPPP55YY?7!~:.                                  \s
                                             .:!?J5PPPPP55555YY555555PPPPPGGGGGPP5J7~:.                            \s
                                         .~7YPPP5YJJJJJJJJYYYYY555555PPPPPGGGGGGGGGGGG5Y7^.                        \s
                                      :7YPP5YJ????JJJJJYYYYYY55555PPPPPPGGGGGGGGGGGGGGGGGGPY!:                     \s
                                   :75GPYJ??????JJJJJYYYYY555555PPPPPPGGGGGGGGGGGGGGGGGGGGGGGGY7:                  \s
                                .!YPPY?77?????JJJJJYYYYY55555PPPPPPGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGY!.               \s
                              :7PGY?77?????JJJJJJYYYYYYYJ?7!!!!~~!!!!!!77?JYPPGBGGGGGGGGGGGGGGGGGGGP7:             \s
                            :?GGY777?????JJJJJYYYY?7!~~~~!77??JJJJJJJJ??77!!~~~7J5PGBGGGGGGGGGGGGGGGGP?:           \s
                          .7GGJ777?????JJJJYYY?!~^~7?JJYYYYYYYYYYYYYYYYYYYYYYYJ?!~~!?5GGGGGGGGGGGGGGGGGP7.         \s
                         ~PGY?77?????JJJJYY?!^~!?JYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYJ?!~~7YGGGGGGGGGGGGGGGG5~        \s
                       .?GP?7??????JJJJYJ!^^7JYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYJ!^~JGGGGGGGGGGGGGGGG?.      \s
                      :5GY7??????JJJJYJ~^~?YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYJYYY?~^?GGGGGGGGGGGGGGB5:     \s
                     ~PGJ7?????JJJJJY!:~JYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYJJJY?~:?GGGGGGGGGGGGGGP^    \s
                    ~GPJ?????JJJJYY?^^?5YYYYY7!!!!!!!!!7!7?YYYYYYYYYYYYYYJ!!!!!!!!!!!!!JJJ?JY?^^5GGGGGGGGGGGGBP~   \s
                   ^PGJ??J?JJJJJYY!:!YYYYYYYJ7~:          .~JYYYYYYYYYYYYJ!~~:     .^~!?JJ???JY!:JGGGGGGGGGGGGGP^  \s
                  .PGJ???JJJJJJYY~.?YYYYYYYYYYYY7:          .!YYYYYYYYYYYYYYYY7.  ~JJJJJJ??????J?.!GGGGGGGGGGGGGP. \s
                 .JBY?JJJJJYYYYY~.JYYYYYYYYYYYYYYY^           :7YYYYYYYYYYJJJJY^ .?JJJJ?????????J?.!GGGGGGGGGGGGBY \s
                 ~GPJJJJJYYYYY57.?YYYYYYYYYYYYYYYY~  .          ^?YYYYYYJJJJJJY^  ?J?????????????J?.7GGGGGGGGGGGGG!\s
                .YBYJJJJYYYYY5J.!YYYYYYYYYYYYYYYYY~  ~~  .        ~JYYJJJJJJJJJ^  ?J?????????????7J7 YBGGGGGGGGGGBY.
                ~GPJJYYYYY5555^:YYYYYYYYYYYYYYYYYY~  75?:          .~JYJJJJJJJJ^  ???????????????7?Y:^GGGGGGGGGGGGG~
                7B5JJYYYY555PY 7YYYYYYYYYYYYYYYYYY~  7YYY7:          .!JJJJJJ?J^  7??????????????77J7 5GGGGGGGGGGGB7
                YG5YYY555555P7.JYYYYYYYYYYYYYYYYYY~  7YYYYJ!.   .      :7JJJ??J^  7???????????77??7?J.7GGGGGGGGGGGGY
                PG5Y555555PPG^^YYYYYYYYYYYYYYYYYYY~  7YYYYYY?~.          ^7JJ?J^  7??????????777777?Y^^BGGGGGGGGGGGP
                GG5Y5555PPPPG^^YYJYYYYYYYYYYYYYYYY~  7YYYYYYJY?^          .^???^  7?????????77777777Y^^BGGGGGGGGGGGG
                PG555PPPPPPPB^^YYYYYYYYYYYYYYYYYYY~  7YYJJJJJJJJ7:          .~?^  7?????77?777777777J^~BGGGGGGGGGGGP
                YGP5PPPPPPGGB7.JYYYYYYYYYYYYYYYYYY~  7YYJJJJJJJJJ?!.          :.  7????777777777777?J.7GGGGGGGGGGGGY
                7BPPPPPGGGGGG5 7YYYYYYYYYYYYYYYYYY~  7YJJJJJJJJJJJJ?~.            7???7777777777777J7 5GGGGGGGGGGGB7
                ^GGPPGGGGGGGGG~:YYYYYYYYYYYYYYYYYY~  7YJJJJJJJJ??????7^           7?777777777777777Y:~GGGGGGGGGGGGG^
                .YGGGGGGGGGGGBY.!YYYYYYYYYYYYYYYJY~  !YJJJJJJJ?????????!:         7?77777777777777J!.YGGGGGGGGGGGBY.
                 ~GGGGGGGGGGGGG?.?YYYYYJYYYYYYYJJY~  !YJJ?J??????????????!.      .7?7777777777777??.?GGGGGGGGGGGGG~\s
                  YGGGGGGGGGGGGG7.?YYYYYYYYYJJJJYJ^  ^JJJJ????????????????7~.     !7777777777777?J.7GGGGGGGGGGGGBY \s
                  :PGGGGGGGGGGGGG7.?YYYJYYJJJ???7^    :!777?????????????????7^.   !7777777777!7J?.7GGGGGGGGGGGGGP: \s
                   ^GGGGGGGGGGGGGG?:!YYYJYJJJ~^^:::::::::^^?????????????7?77?7!:..!777777777!?J!.?GGGGGGGGGGGGGP^  \s
                    ~PGGGGGGGGGGGGG5^^?YJJJJJJJJJJJJ?????????????????77777777777!!77777777!7??^^5GGGGGGGGGGGGGP~   \s
                     ^PBGGGGGGGGGGGGG?:~JYJJJJJJ????????????????????77777777777777777777!7??~:?GGGGGGGGGGGGGGP^    \s
                      :5BGGGGGGGGGGGGBG?^~?JJJ??????????????????7??777777777777777777777?7~^?PBGGGGGGGGGGGGB5^     \s
                       .?GGGGGGGGGGGGGGGPJ^^!?JJJ??????????????777777777777777777!777??!^~JPGGGGGGGGGGGGGGG?.      \s
                         ~5GGGGGGGGGGGGGGGG57~~!?JJ???????????7777777777777777777??7!~~75GGGGGGGGGGGGGGGG5~        \s
                          .7PBGGGGGGGGGGGGGGGG5?!~~!7????????77777777777777????7!~~!?5GGGGGGGGGGGGGGGGBP7.         \s
                            :?PGGGGGGGGGGGGGGGGGGP5?7~~~~!!777????????777!!~~~!7?5PGGGGGGGGGGGGGGGGGGP?:           \s
                              :7PGGGGGGGGGGGGGGGGGGGGGPPYJ??7!!!!!!!!!!7??JY5PGGGGGGGGGGGGGGGGGGGGGP7:             \s
                                .!YPBGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGPY!.               \s
                                   :7YGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGY7:    .             \s
                                      :7JPGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGPJ7^                     \s
                                         .^7Y5GGBGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG5J7~.                        \s
                                             .:~7J5PPGGGGGGGGGGGGGGGGGGGGGGGGGGGP5J?~:.                            \s
                                                   .:~!7?JY55PPGGGGGPPP55YYJ7!~:.                                  \s
                """;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String cardTableRep(GameDTO gameDTO) {
        return "cards on the table:\n" +
                playedCardsFormat(gameDTO) +
                playerHandFormat(gameDTO);
    }

    private String playedCardsFormat(GameDTO gameDTO) {
        StringBuilder builder = new StringBuilder();
        for (DashboardDTO playerDashBoard : gameDTO.getDashboards().values()) {
            if (playerDashBoard.hasPlayedCard()) {
                String playerName = playerDashBoard.getOwner();
                int cardId = playerDashBoard.getPlayedCards().getLast();
                builder.append(String.format("%s played : %s%n", playerName, cardFormat(cardId)));
            }
        }
        return builder.toString();
    }

    private String playerHandFormat(GameDTO gameDTO) {
        StringBuilder builder = new StringBuilder("your hand :\n");
        List<Integer> hand = gameDTO.getDashboards().get(yourName).getTheHand();
        hand.forEach(card -> builder.append(String.format("%s  ", cardFormat(card))));
        builder.append('\n');
        return builder.toString();

    }

    private String cardFormat(int index) {
        return String.format("<%d, %d>", index, index / 2 + index % 2);
    }

    public String cloudsFormat(GameDTO gameDTO) {
        List<String> cloudsAsStrings = new ArrayList<>();
        for (int i = 0; i < gameDTO.getClouds().size(); i++) {
            cloudsAsStrings.add(cloudFormat(gameDTO.getClouds().get(i), i));
        }
        return StringParallelizer.convertInParallel(cloudsAsStrings);
    }

    public String characterCardsFormat(GameDTO gameDTO) {
        List<String> cardsAsStrings = new ArrayList<>();
        for (int i = 0; i < gameDTO.getCharacterCards().size(); i++) {
            cardsAsStrings.add(characterCardFormat(gameDTO.getCharacterCards().get(i)));
        }
        return StringParallelizer.convertInParallel(cardsAsStrings);
    }

    private String vanillaCollectionFormat(Map<TokenEnum, Integer> map, String name) {
        String edge = String.format(            " _______________%n");
        String noColumnFiller = String.format(  "|_______________|%n");
        String filler = String.format(          "|_______|_______|%n");

        StringBuilder builder = new StringBuilder();
        builder.append(edge);
        builder.append(String.format("|%-15s|%n", name));
        builder.append(noColumnFiller);
        builder.append(String.format("|       |AMOUNT |%n"));
        builder.append(filler);
        String additional = "";
        for (int i = 0; i < TokenEnum.values().length; i++) {
            TokenEnum colour = TokenEnum.values()[i];
            builder.append(String.format("|%-7s| x%-2d   |%s%n", colour, map.get(colour), additional));
        }
        builder.append(filler);
        return builder.toString();
    }

    public String cloudFormat(Map<TokenEnum, Integer> map, Integer cloudID){
        return vanillaCollectionFormat(map, String.format("Cloud#%d", cloudID));
    }

    public String characterCardFormat(CharacterCardDTO characterCardDTO){
        return vanillaCollectionFormat(characterCardDTO.getTokens(), String.format("%s(%d)%s", characterCardDTO.getCardName(), characterCardDTO.getCost(), characterCardDTO.isActive()?"-Active" : ""));
    }

    public String islandsRep(GameDTO gameDTO) {
        List<String> islands = gameDTO.getIslands().stream().map(this::islandFormat).collect(Collectors.toList());
        List<String> firstHalf = islands.subList(0, islands.size() / 2);
        List<String> secondHalf = islands.subList(islands.size() / 2, islands.size());
        Collections.reverse(secondHalf);
        return StringParallelizer.convertInParallel(firstHalf)
                +
                StringParallelizer.convertInParallel(secondHalf);
    }

    private String islandFormat(IslandDTO island) {
        String edge = String.format(" _______________________%n");
        String noColumnFiller = String.format("|_______________________|%n");
        String filler = String.format("|_______|_______|_______|%n");

        StringBuilder builder = new StringBuilder();
        builder.append(edge);
        builder.append(String.format("| island %-15s|%n", island.getIndex() + (island.hasMotherNature() ? " \\O.o/" : "")));
        builder.append(noColumnFiller);
        builder.append(String.format("|       |AMOUNT |SIZE   |%n"));
        builder.append(filler);
        String additional;
        for (int i = 0; i < TokenEnum.values().length; i++) {
            TokenEnum colour = TokenEnum.values()[i];
            additional = switch (i) {
                case 0 -> String.format("%-7s|", " x" + island.getSize());
                case 1, 3 -> "_______|";
                case 2 -> "COLOUR |";
                case 4 -> String.format(" %-6s|", island.getTowerColour().toString().toLowerCase());
                default -> "       |";
            };

            builder.append(String.format("|%-7s| x%-2d   |%s%n", colour, island.getTokens().get(colour), additional));

        }
        builder.append(filler);
        return builder.toString();


    }

    public String dashBoardsRep(GameDTO gameDTO) {
        List<String> boardsReps = new ArrayList<>();
        for (DashboardDTO dashboard : gameDTO.getDashboards().values()) {
            boardsReps.add(dashBoardFormat(dashboard));
        }
        return StringParallelizer.convertInParallel(boardsReps);
    }

    private String dashBoardFormat(DashboardDTO dashboard) {
        String edge = String.format(" _______________________________________ %n");
        String noColumnFiller = String.format("|_______________________________________|%n");
        String filler = String.format("|_______|_______|_______|_______________|%n");

        StringBuilder builder = new StringBuilder();
        builder.append(edge);
        builder.append(String.format("|%-39s|%n", dashboard.getOwner() + "'s dashboard"));
        builder.append(noColumnFiller);
        builder.append(String.format("|       |EH     |SH     |TOWERS         |%n"));
        builder.append(filler);
        for (int i = 0; i < TokenEnum.values().length; i++) {
            TokenEnum colour = TokenEnum.values()[i];
            int inEntranceHall = dashboard.getEntranceHall().get(colour);
            int inStudyHall = dashboard.getStudyHall().get(colour);
            String additional = switch (i) {
                case 0 -> String.format("%-15s", dashboard.getTowerColour().toString().toLowerCase() + " : " + dashboard.getTowers());
                case 1, 3 -> "_______________";
                case 2 -> String.format("%-15s", "COINS");
                case 4 -> String.format("%-15d", dashboard.getCoins());
                default -> "               ";
            };
            String hasProfessorStatus = dashboard.getProfessors().contains(colour) ? " P" : "";
            String formattedString = String.format("|%-7s| x%-2d   | x%-5s|%s|%n", colour, inEntranceHall, inStudyHall + hasProfessorStatus, additional);

            builder.append(formattedString);
        }
        builder.append(filler);
        return builder.toString();
    }
}
