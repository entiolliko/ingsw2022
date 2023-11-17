package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.model.visitor.base_commands.FillClouds;
import it.polimi.ingsw.model.visitor.base_commands.MoveToEntranceHall;
import it.polimi.ingsw.model.cardhandler.CardHandler;
import it.polimi.ingsw.model.character_cards.*;
import it.polimi.ingsw.model.custom_data_structures.MagicList;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;
import it.polimi.ingsw.model.custom_data_structures.exceptions.ChoosingCardException;
import it.polimi.ingsw.model.custom_data_structures.exceptions.NegativeAmountException;
import it.polimi.ingsw.model.dashboard.DashBoard;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.GameEventReceiver;
import it.polimi.ingsw.model.islands.IslandChain;
import org.jetbrains.annotations.NotNull;

import javax.naming.directory.InvalidAttributesException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board extends ModelEventCreator implements Simplifiable {
    private static final List<String> CHARACTER_CARDS = List.of("Card1", "Card2", "Card4", "Card6", "Card7", "Card8", "Card9", "Card11");
    private static final Random RANDOM = new Random();

    private final List<Cloud> clouds;
    private final IslandChain islands;
    private final Map<String, DashBoard> dashBoards;
    private final CardHandler cardHandler;
    private final TokenCollection bag;
    private final Professors professors;
    private final List<String> playersID;
    private final InfluenceHandler influenceHandler;
    private final MagicList characterCardList;

    public Board(List<String> playersID, List<String> magicians, List<String> squadNames, Boolean isGameExpert) {
        this.islands = IslandChain.newDefaultIslandChain(mapSquadToPlayer(squadNames, playersID));

        this.playersID = playersID;

        this.bag = TokenCollection.newDefaultBag();

        this.cardHandler = new CardHandler(playersID);

        this.dashBoards = new HashMap<>();
        IntStream.range(0, playersID.size())
                .forEach(i -> this.dashBoards.put(
                        playersID.get(i),
                        new DashBoard(playersID.get(i), magicians.get(i), squadNames.get(i))));

        this.clouds = new ArrayList<>();

        playersID.forEach(playerID -> this.clouds.add(new Cloud(playersID.size() == 3 ? 4 : 3)));

        this.professors = new Professors();

        this.influenceHandler = new InfluenceHandler();

        setTokenToIslandInSetUp();

        setTokenToEntranceSetUp();

        (new FillClouds()).visit(this);

        try {
            characterCardList = new MagicList();
            if (Boolean.TRUE.equals(isGameExpert))
                generateRandomCharacterCards().forEach(characterCardList::add);
        } catch (Exception e) {
            throw new ChoosingCardException("There has been an error when choosing the random character cards!");
        }

    }

    /**
     * Accepts the Visitor Command
     *
     * @param visitor The command to be accepted
     * @throws ReloadGameException Thrown when the command cannot be executed
     */
    public void accept(@NotNull VisitorCommand visitor) throws ReloadGameException {
        try {
            visitor.visit(this);
        } catch (RuntimeException e) {
            throw new ReloadGameException(e.getMessage(), e);
        }

    }

    /**
     * Maps each player to their respective squad ID
     *
     * @param squadNames The names of the squads active in the current game
     * @param playersID  The IDs of all the players in the current game
     * @return Returns the map of the Squad Names to the player IDs
     */
    private Map<String, List<String>> mapSquadToPlayer(List<String> squadNames, @NotNull List<String> playersID) {
        //alternative
        // map squadID : playerID
        Map<String, String> squadMap = IntStream.range(0, playersID.size())
                .collect(HashMap::new, (map, index) -> map.put(playersID.get(index), squadNames.get(index)), Map::putAll);
        // group map by squadID

        return squadMap.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    /**
     * Returns the Clouds in the current game
     *
     * @return The Clouds in the current game
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Returns the Islands in the current game
     *
     * @return The Islands in the current game
     */
    public IslandChain getIslands() {
        return islands;
    }

    /**
     * Returns the Dashboards in the current game
     *
     * @return The Dashboards in the current game
     */
    public Map<String, DashBoard> getDashBoards() {
        return dashBoards;
    }

    /**
     * Return the Card Handler of the game
     *
     * @return The Card Handler
     */
    public CardHandler getCardHandler() {
        return cardHandler;
    }

    /**
     * Returns the bag of the game
     *
     * @return The bag
     */
    public TokenCollection getBag() {
        return bag;
    }

    /**
     * Returns the Professors in the game
     *
     * @return The Professors
     */
    public Professors getProfessors() {
        return professors;
    }

    /**
     * Returns the ID of each player
     *
     * @return The list of IDs
     */
    public List<String> getPlayersID() {
        return playersID;
    }

    /**
     * Returns the Influence Handler
     *
     * @return The Influence Handler
     */
    public InfluenceHandler getInfluenceHandler() {
        return this.influenceHandler;
    }

    /**
     * Returns the Character Cards that have been randomly chosen
     *
     * @return The list of the 3 character cards
     */
    public List<CharacterCard> getCharacterCardList() {
        return this.characterCardList;
    }

    private void setTokenToIslandInSetUp() {

        TokenCollection temp = TokenCollection.newEmptyCollection();

        Stream.of(TokenEnum.values())
                .forEach(
                        colour -> temp.addToCollection(bag.popToken(colour, 2))
                )
        ;
        for (int i = 0; i < this.islands.numberOfIslands(); i++)
            if (i != this.islands.getCurrMotherNaturePos() && i != (this.islands.getCurrMotherNaturePos() + this.islands.numberOfIslands() / 2) % this.islands.numberOfIslands())
                this.islands.addTokens(temp.randomPop(1), i);

    }

    private void setTokenToEntranceSetUp() {
        for (int i = 0; i < this.dashBoards.size(); i++) {
            for (int j = 0; j < (this.playersID.size() == 3 ? 9 : 7); j++) {
                try {
                    this.accept(new MoveToEntranceHall(playersID.get(i)));
                } catch (Exception e) {
                    throw new NegativeAmountException();
                }
            }
        }
    }

    private List<CharacterCard> generateRandomCharacterCards() throws InvalidAttributesException {
        List<CharacterCard> result = new ArrayList<>();
        while (result.size() < 3) {
            int i = RANDOM.nextInt(0, 8);
            String cardName = CHARACTER_CARDS.get(i);
            if (!result.stream().map(x -> x.getClass().getSimpleName()).toList().contains(cardName)) {
                switch (cardName) {
                    case "Card1":
                        result.add(new Card1(bag.randomPop(4)));
                        break;
                    case "Card2":
                        result.add(new Card2());
                        break;
                    case "Card4":
                        result.add(new Card4());
                        break;
                    case "Card6":
                        result.add(new Card6());
                        break;
                    case "Card7":
                        result.add(new Card7(bag.randomPop(6)));
                        break;
                    case "Card8":
                        result.add(new Card8());
                        break;
                    case "Card9":
                        result.add(new Card9());
                        break;
                    case "Card11":
                        result.add(new Card11(bag.randomPop(4)));
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Board{" + "\n\t" +
                "clouds=" + clouds + "\n\t" +
                ", islands=" + islands + "\n\t" +
                ", dashBoards=" + dashBoards + "\n\t" +
                ", cardHandler=" + cardHandler + "\n\t" +
                ", bag=" + bag + "\n\t" +
                ", professors=" + professors + "\n\t" +
                ", playersID=" + playersID + "\n\t" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(clouds, board.clouds) && Objects.equals(islands, board.islands) && Objects.equals(dashBoards, board.dashBoards) && Objects.equals(cardHandler, board.cardHandler) && Objects.equals(bag, board.bag) && Objects.equals(professors, board.professors) && Objects.equals(playersID, board.playersID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clouds, islands, dashBoards, cardHandler, bag, professors, playersID);
    }

    @Override
    public void fillDTO(GameDTO gameDTO) {
        gameDTO.setBagSize(this.getBag().size());
        clouds.forEach(cloud -> cloud.fillDTO(gameDTO));
        islands.fillDTO(gameDTO);
        addDashboardDTOs(gameDTO);
        professors.fillDTO(gameDTO);
        cardHandler.fillDTO(gameDTO);
        characterCardList.forEach(card -> card.fillDTO(gameDTO));
    }

    private void addDashboardDTOs(GameDTO gameDTO) {
        dashBoards.forEach((player, dashBoard) -> dashBoard.fillDTO(gameDTO));
    }

    @Override
    public void addEventObserver(GameEventReceiver eventObserver) {

        super.addEventObserver(eventObserver);
        this.cardHandler.addEventObserver(eventObserver);
        this.professors.addEventObserver(eventObserver);
        this.islands.addEventObserver(eventObserver);
        this.dashBoards.values().forEach(dashBoard -> dashBoard.addEventObserver(eventObserver));
    }

    @Override
    public Set<GameEventReceiver> popReceivers() {
        this.cardHandler.popReceivers();
        this.professors.popReceivers();
        this.islands.popReceivers();
        this.dashBoards.values().forEach(ModelEventCreator::popReceivers);
        return super.popReceivers();
    }
}

