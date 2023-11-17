package it.polimi.ingsw.client.frontend.gui.screen_controllers;

import it.polimi.ingsw.client.frontend.gui.Properties;
import it.polimi.ingsw.client.frontend.gui.rocket_science.MatheMagic;
import it.polimi.ingsw.client.frontend.gui.rocket_science.Position;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.*;
import it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.character_cards.PlayCharacterCardReq;
import it.polimi.ingsw.controller.data_transfer_objects.*;
import it.polimi.ingsw.controller.utility.ConnectionStatusEnum;
import it.polimi.ingsw.model.dashboard.TokenEnum;
import it.polimi.ingsw.model.game_event.game_events.*;
import it.polimi.ingsw.model.game_event.game_events.character_cards.*;
import it.polimi.ingsw.model.islands.TowerEnum;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.lang.System.exit;

public class MainGameScreenController implements Initializable {
    private Properties properties = Properties.getInstance();

    private ImageView entranceHallChosenStudent;
    private ImageView card1ChosenToken;
    private List<ImageView> card7ToAdd;
    private List<ImageView> card7ToRemove;
    private String oldGameState;
    private String oldCurrentPlayer;
    private Effect oldCardEffect;


    private List<String> orderedPlayerIDs = new ArrayList<>();
    private Map<String, ImageView> playersDashboardImages = new HashMap<>();
    private HashMap<String, Label> labelUsernames = new HashMap<>();

    //************My Dashboard*************
    private List<ImageView> myEntranceHall = new ArrayList<>();
    private List<GridPane> myStudyHall = new ArrayList<>();
    private Map<TokenEnum, List<ImageView>> myStudyHallByTokenColor = new HashMap<>();
    private List<ImageView> myGreenStudyHall = new ArrayList<>();
    private List<ImageView> myRedStudyHall = new ArrayList<>();
    private List<ImageView> myYellowStudyHall = new ArrayList<>();
    private List<ImageView> myPinkStudyHall = new ArrayList<>();
    private List<ImageView> myBlueStudyHall = new ArrayList<>();

    private Map<TokenEnum, ImageView> myProfessorHall = new HashMap<>();
    private List<ImageView> myTowerHall = new ArrayList<>();
    //************END*************

    //************Other Player's Dashboards*************
    private Map<String, Map<TokenEnum, Label>> entranceHallsD = new HashMap<>();
    private Map<String, Map<TokenEnum, ImageView>> entranceHallsDImage = new HashMap<>();

    private Map<TokenEnum, Label> entranceHallD0 = new HashMap<>();
    private Map<TokenEnum, ImageView> entranceHallD0Image = new HashMap<>();

    private Map<TokenEnum, Label> entranceHallD1 = new HashMap<>();
    private Map<TokenEnum, ImageView> entranceHallD1Image = new HashMap<>();

    private Map<TokenEnum, Label> entranceHallD2 = new HashMap<>();
    private Map<TokenEnum, ImageView> entranceHallD2Image = new HashMap<>();

    private Map<String, Map<TokenEnum, Label>> studyHallsD = new HashMap<>();
    private Map<TokenEnum, Label> studyHallD0 = new HashMap<>();
    private Map<TokenEnum, Label> studyHallD1 = new HashMap<>();
    private Map<TokenEnum, Label> studyHallD2 = new HashMap<>();

    private Map<String, Label> coinsD = new HashMap<>();
    private Map<String, ImageView> towersD = new HashMap<>();
    private Map<String, Label> towerNumberD = new HashMap<>();
    //************END*************

    //************Islands*************
    private Map<Integer, StackPane> islandsStackPane = new HashMap<>();
    private Map<Integer, ImageView> islandBackgroundImage = new HashMap<>();
    private Map<Integer, Label> islandIndexLabel = new HashMap<>();
    private Map<Integer, Map<Integer, Label>> islandsLabel = new HashMap<>();
    private Map<Integer, Label> island0 = new HashMap<>();
    private Map<Integer, Label> island1 = new HashMap<>();
    private Map<Integer, Label> island2 = new HashMap<>();
    private Map<Integer, Label> island3 = new HashMap<>();
    private Map<Integer, Label> island4 = new HashMap<>();
    private Map<Integer, Label> island5 = new HashMap<>();
    private Map<Integer, Label> island6 = new HashMap<>();
    private Map<Integer, Label> island7 = new HashMap<>();
    private Map<Integer, Label> island8 = new HashMap<>();
    private Map<Integer, Label> island9 = new HashMap<>();
    private Map<Integer, Label> island10 = new HashMap<>();
    private Map<Integer, Label> island11 = new HashMap<>();

    private Map<Integer, ImageView> motherNatureImages = new HashMap<>();
    private Map<Integer, ImageView> towerIslandsImages = new HashMap<>();
    //************END*************

    //************Clouds*************
    private List<StackPane> cloudsStackPane = new ArrayList<>();
    private List<List<ImageView>> clouds = new ArrayList<>();
    private List<ImageView> cloud0 = new ArrayList<>();
    private List<ImageView> cloud1 = new ArrayList<>();
    private List<ImageView> cloud2 = new ArrayList<>();
    private List<ImageView> cloud3 = new ArrayList<>();
    //************END*************

    //************Cards*************
    private Map<String, ImageView> playedASCsMapByUsername = new HashMap<>();
    private List<ImageView> paneAssistantCards = new ArrayList<>();

    private Map<String, ImageView> ccCardsByName = new HashMap<>();
    private Map<String, ImageView> ccCardCoinsByName = new HashMap<>();
    private List<ImageView> card1Tokens = new ArrayList<>();
    private List<ImageView> card7Tokens = new ArrayList<>();
    private List<ImageView> card7EHTokens = new ArrayList<>();
    private List<ImageView> card11Tokens = new ArrayList<>();
    //************END*************

    private List<Label> lobbyStandByPs = new ArrayList<>();


    @FXML private AnchorPane rootPane;
    @FXML private Label PlayerTurnIndicator;
    @FXML private ImageView myDashboardImage;
    //**************Game is Over*******************
    @FXML private Pane GameIsOverPane;
    @FXML private Label GameIsOverLabel;
    //*********************************

    //**************Lobby Stand By*******************
    @FXML private Pane LobbyStandByPane;
    @FXML private TilePane LobbyStandByTilePane;
    @FXML private Label LobbyStandByP0, LobbyStandByP1, LobbyStandByP2, LobbyStandByP3;
    //*********************************

    //Islands
    //*********************************
    @FXML private StackPane IStackPaneZero;
    @FXML private Label IslandIndexLabel0;
    @FXML private ImageView IslandZero;
    @FXML private ImageView MNZero;
    @FXML private ImageView TowerZero;
    @FXML private Label I0GT, I0RT, I0YT, I0PT, I0BT;
    //*********************************
    @FXML private StackPane IStackPaneOne;
    @FXML private Label IslandIndexLabel1;
    @FXML private ImageView IslandOne;
    @FXML private ImageView MNOne;
    @FXML private ImageView TowerOne;
    @FXML private Label I1GT, I1RT, I1YT, I1PT, I1BT;
    //*********************************
    @FXML private StackPane IStackPaneTwo;
    @FXML private Label IslandIndexLabel2;
    @FXML private ImageView IslandTwo;
    @FXML private ImageView MNTwo;
    @FXML private ImageView TowerTwo;
    @FXML private Label I2GT, I2RT, I2YT, I2PT, I2BT;
    //*********************************
    @FXML private StackPane IStackPaneThree;
    @FXML private Label IslandIndexLabel3;
    @FXML private ImageView IslandThree;
    @FXML private ImageView MNThree;
    @FXML private ImageView TowerThree;

    @FXML private Label I3GT, I3RT, I3YT, I3PT, I3BT;
    //*********************************
    @FXML private StackPane IStackPaneFour;
    @FXML private Label IslandIndexLabel4;
    @FXML private ImageView IslandFour;
    @FXML private ImageView MNFour;
    @FXML private ImageView TowerFour;
    @FXML private Label I4GT, I4RT, I4YT, I4PT, I4BT;
    //*********************************
    @FXML private StackPane IStackPaneFive;
    @FXML private Label IslandIndexLabel5;
    @FXML private ImageView IslandFive;
    @FXML private ImageView MNFive;
    @FXML private ImageView TowerFive;
    @FXML private Label I5GT, I5RT, I5YT, I5PT, I5BT;
    //*********************************
    @FXML private StackPane IStackPaneSix;
    @FXML private Label IslandIndexLabel6;
    @FXML private ImageView IslandSix;
    @FXML private ImageView MNSix;
    @FXML private ImageView TowerSix;
    @FXML private Label I6GT, I6RT, I6YT, I6PT, I6BT;
    //*********************************
    @FXML private StackPane IStackPaneSeven;
    @FXML private Label IslandIndexLabel7;
    @FXML private ImageView IslandSeven;
    @FXML private ImageView MNSeven;
    @FXML private ImageView TowerSeven;

    @FXML private Label I7GT, I7RT, I7YT, I7PT, I7BT;
    //*********************************
    @FXML private StackPane IStackPaneEight;
    @FXML private Label IslandIndexLabel8;
    @FXML private ImageView IslandEight;
    @FXML private ImageView MNEight;
    @FXML private ImageView TowerEight;
    @FXML private Label I8GT, I8RT, I8YT, I8PT, I8BT;
    //*********************************
    @FXML private StackPane IStackPaneNine;
    @FXML private Label IslandIndexLabel9;
    @FXML private ImageView IslandNine;
    @FXML private ImageView MNNine;
    @FXML private ImageView TowerNine;
    @FXML private Label I9GT, I9RT, I9YT, I9PT, I9BT;
    //*********************************
    @FXML private StackPane IStackPaneTen;
    @FXML private Label IslandIndexLabel10;
    @FXML private ImageView IslandTen;
    @FXML private ImageView MNTen;
    @FXML private ImageView TowerTen;
    @FXML private Label I10GT, I10RT, I10YT, I10PT, I10BT;
    //*********************************
    @FXML private StackPane IStackPaneEleven;
    @FXML private Label IslandIndexLabel11;
    @FXML private ImageView IslandEleven;
    @FXML private ImageView MNEleven;
    @FXML private ImageView TowerEleven;
    @FXML private Label I11GT, I11RT, I11YT, I11PT, I11BT;
    //*********************************

    //Character Cards
    //*********************************
    @FXML private Label CoinLabel;
    @FXML private ImageView CoinImage;
    @FXML private HBox CharacterCardsBox;
    @FXML private ImageView CharacterCardZero, CharacterCardOne, CharacterCardTwo, CharacterCardZeroCoin, CharacterCardOneCoin, CharacterCardTwoCoin;
    //*********************************

    //Clouds
    //*********************************
    @FXML private GridPane CloudsGrid;
    //*********************************
    @FXML private StackPane CStackPaneZero;
    @FXML private ImageView CloudZero;
    @FXML private ImageView TokenZeroCloudZero, TokenOneCloudZero, TokenTwoCloudZero, TokenThreeCloudZero;
    //*********************************
    //*********************************
    @FXML private StackPane CStackPaneOne;
    @FXML private ImageView CloudOne;
    @FXML private ImageView TokenZeroCloudOne, TokenOneCloudOne, TokenTwoCloudOne, TokenThreeCloudOne;
    //*********************************
    //*********************************
    @FXML private StackPane CStackPaneTwo;
    @FXML private ImageView CloudTwo;
    @FXML private ImageView TokenZeroCloudTwo, TokenOneCloudTwo, TokenTwoCloudTwo, TokenThreeCloudTwo;
    //*********************************
    //*********************************
    @FXML private StackPane CStackPaneThree;
    @FXML private ImageView CloudThree;
    @FXML private ImageView TokenZeroCloudThree, TokenOneCloudThree, TokenTwoCloudThree;
    //*********************************
    //*********************************

    @FXML private ImageView PlayedASC;

    //Entrance Hall
    //*******************
    @FXML private ImageView EH0,EH1,EH2,EH3,EH4,EH5,EH6,EH7,EH8;
    //*******************

    //Green Study Hall
    //*******************
    @FXML private GridPane GreenStudyHall;
    @FXML private ImageView GSH0,GSH1,GSH2,GSH3,GSH4,GSH5,GSH6,GSH7,GSH8,GSH9;
    //*******************

    //Red Study Hall
    //*******************
    @FXML private GridPane RedStudyHall;
    @FXML private ImageView RSH0,RSH1,RSH2,RSH3,RSH4,RSH5,RSH6,RSH7,RSH8,RSH9;
    //*******************

    //Yellow Study Hall
    //*******************
    @FXML private GridPane YellowStudyHall;
    @FXML private ImageView YSH0,YSH1,YSH2,YSH3,YSH4,YSH5,YSH6,YSH7,YSH8,YSH9;
    //*******************

    //Pink Study Hall
    //*******************
    @FXML private GridPane PinkStudyHall;
    @FXML private ImageView PSH0,PSH1,PSH2,PSH3,PSH4,PSH5,PSH6,PSH7,PSH8,PSH9;
    //*******************

    //Blue Study Hall
    //*******************
    @FXML private GridPane BlueStudyHall;
    @FXML private ImageView BSH0,BSH1,BSH2,BSH3,BSH4,BSH5,BSH6,BSH7,BSH8,BSH9;
    //*******************

    //ProfessorHall
    //*******************
    @FXML private ImageView PrH0,PrH1,PrH2,PrH3,PrH4;
    //*******************

    //Tower Hall
    //*******************
    @FXML private ImageView TH0,TH1,TH2,TH3,TH4,TH5,TH6,TH7,TH8;
    //*******************

    //********Player's 0 Dashboard***********
    @FXML private Label DUsername0;
    @FXML private StackPane DStackPane0;
    @FXML private ImageView Player0DashboardImage;
    @FXML private ImageView D0GreenEH, D0RedEH, D0YellowEH, D0PinkEH, D0BlueEH;
    @FXML private Label GreenEHD0,RedEHD0,YellowEHD0,PinkEHD0,BlueEHD0,GreenSHD0,RedSHD0,YellowSHD0,PinkSHD0,BlueSHD0;

    @FXML private ImageView PlayedACD0;
    @FXML private ImageView TowerImageD0;
    @FXML private Label TowersD0;
    @FXML private Label CoinsD0;
    //*******************

    //********Player's 1 Dashboard***********
    @FXML private Label DUsername1;
    @FXML private StackPane DStackPane1;
    @FXML private ImageView Player1DashboardImage;
    @FXML private ImageView D1GreenEH, D1RedEH, D1YellowEH, D1PinkEH, D1BlueEH;
    @FXML private Label GreenEHD1, RedEHD1, YellowEHD1, PinkEHD1, BlueEHD1, GreenSHD1, RedSHD1, YellowSHD1, PinkSHD1, BlueSHD1;
    @FXML private ImageView PlayedACD1;
    @FXML private ImageView TowerImageD1;
    @FXML private Label TowersD1;
    @FXML private Label CoinsD1;
    //*******************

    //********Player's 2 Dashboard***********
    @FXML private Label DUsername2;
    @FXML private StackPane DStackPane2;
    @FXML private ImageView Player2DashboardImage;
    @FXML private ImageView D2GreenEH, D2RedEH, D2YellowEH, D2PinkEH, D2BlueEH;
    @FXML private Label GreenEHD2, RedEHD2, YellowEHD2, PinkEHD2, BlueEHD2, GreenSHD2, RedSHD2, YellowSHD2, PinkSHD2, BlueSHD2;
    @FXML private ImageView PlayedACD2;
    @FXML private ImageView TowerImageD2;
    @FXML private Label TowersD2;
    @FXML private Label CoinsD2;
    //*******************

    //*********Assistant Cards Pane/Screen**********
    @FXML private ImageView ASC0, ASC1, ASC2, ASC3;
    @FXML private Pane ASCPane;
    @FXML private ImageView PASC0, PASC1, PASC2, PASC3, PASC4, PASC5, PASC6, PASC7, PASC8, PASC9;
    @FXML private Button ASCPPlayButton;
    @FXML private Button ASCPCloseButton;
    //*******************

    //********Character Card1***********
    @FXML private Pane Card1Pane;
    @FXML private ImageView Card1Token0, Card1Token1, Card1Token2, Card1Token3;
    @FXML private ChoiceBox<Integer> Card1ChoiceBox;
    //*******************

    //********Character Card7***********
    @FXML private Pane Card7Pane;
    @FXML private ImageView Card7T0, Card7T1, Card7T2, Card7T3, Card7T4, Card7T5;
    @FXML private ImageView Card7EHT0, Card7EHT1, Card7EHT2, Card7EHT3, Card7EHT4, Card7EHT5, Card7EHT6, Card7EHT7, Card7EHT8;
    //*******************

    //********Character Card9***********
    @FXML private Pane Card9Pane;
    //*******************

    //********Character Card11***********
    @FXML private Pane Card11Pane;
    @FXML private ImageView Card11T0, Card11T1, Card11T2, Card11T3;
    //*******************


    //*******************Initial Set-Up*******************
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setUpLists();

        if(properties.isLobbyIsInStandBy()) {
            lobbyStandby(properties.getPlayersConnections());
        }

        for (int i = 0; i < 12; i++)
            Card1ChoiceBox.getItems().add(i + 1);

        entranceHallChosenStudent = null;
        card1ChosenToken = null;
        card7ToAdd = new ArrayList<>();
        card7ToRemove = new ArrayList<>();
        oldGameState = properties.getGameDTO().getGamePhase();
        oldCurrentPlayer = properties.getGameDTO().getCurrentPlayer();
        changeCurrentPlayerIndicator(oldCurrentPlayer, oldGameState);
        oldCardEffect = null;

        for(ImageView image : Arrays.asList(ASC0, ASC1, ASC2, ASC3))
            image.setImage(new Image(getMagicianImagePathFromName(properties.getMagician())));

        if (properties.getGameDTO().getCharacterCards().size() == 0)
            setUpNormalGame();
        else
            setUpExpertGame();

        switch (properties.getGameDTO().getDashboards().keySet().size()) {
            case 2:
                setUpTwoPlayers();
                break;
            case 3:
                setUpThreePlayers();
                break;
        }

        readPlayersDashboard();
        readClouds();
        readAssistantCards();
        readIslands();


    }

    private void setUpLists() {

        orderedPlayerIDs.addAll(properties.getPlayers());
        orderedPlayerIDs.remove(properties.getUsername());
        orderedPlayerIDs.add(0, properties.getUsername());

        for (String string : orderedPlayerIDs)
            playersDashboardImages.put(string, Arrays.asList(myDashboardImage, Player0DashboardImage, Player1DashboardImage, Player2DashboardImage).get(orderedPlayerIDs.indexOf(string)));

        //*************My Entrance Hall*************
        myEntranceHall.addAll(Arrays.asList(EH0, EH1, EH2, EH3, EH4, EH5, EH6, EH7, EH8));

        myGreenStudyHall.addAll(Arrays.asList(GSH0, GSH1, GSH2, GSH3, GSH4, GSH5, GSH6, GSH7, GSH8, GSH9));
        myRedStudyHall.addAll(Arrays.asList(RSH0, RSH1, RSH2, RSH3, RSH4, RSH5, RSH6, RSH7, RSH8, RSH9));
        myYellowStudyHall.addAll(Arrays.asList(YSH0, YSH1, YSH2, YSH3, YSH4, YSH5, YSH6, YSH7, YSH8, YSH9));
        myPinkStudyHall.addAll(Arrays.asList(PSH0, PSH1, PSH2, PSH3, PSH4, PSH5, PSH6, PSH7, PSH8, PSH9));
        myBlueStudyHall.addAll(Arrays.asList(BSH0, BSH1, BSH2, BSH3, BSH4, BSH5, BSH6, BSH7, BSH8, BSH9));
        myStudyHall.addAll(Arrays.asList(GreenStudyHall, RedStudyHall, YellowStudyHall, PinkStudyHall, BlueStudyHall));
        myTowerHall.addAll(Arrays.asList(TH0, TH1, TH2, TH3, TH4, TH5, TH6, TH7, TH8));

        for (int i = 0; i < 5; i++) {
            myStudyHallByTokenColor.put(TokenEnum.values()[i], Arrays.asList(myGreenStudyHall, myRedStudyHall, myYellowStudyHall, myPinkStudyHall, myBlueStudyHall).get(i));
            myProfessorHall.put(TokenEnum.values()[i], Arrays.asList(PrH0, PrH1, PrH2, PrH3, PrH4).get(i));
        }//*************END*************

        //*************Other player's dashboard*************
        for (int i = 0; i < TokenEnum.values().length; i++){
            entranceHallD0.put(TokenEnum.values()[i], Arrays.asList(GreenEHD0, RedEHD0, YellowEHD0, PinkEHD0, BlueEHD0).get(i));
            entranceHallD1.put(TokenEnum.values()[i], Arrays.asList(GreenEHD1, RedEHD1, YellowEHD1, PinkEHD1, BlueEHD1).get(i));
            entranceHallD2.put(TokenEnum.values()[i], Arrays.asList(GreenEHD2, RedEHD2, YellowEHD2, PinkEHD2, BlueEHD2).get(i));

            entranceHallD0Image.put(TokenEnum.values()[i], Arrays.asList(D0GreenEH, D0RedEH, D0YellowEH, D0PinkEH, D0BlueEH).get(i));
            entranceHallD1Image.put(TokenEnum.values()[i], Arrays.asList(D1GreenEH, D1RedEH, D1YellowEH, D1PinkEH, D1BlueEH).get(i));
            entranceHallD2Image.put(TokenEnum.values()[i], Arrays.asList(D2GreenEH, D2RedEH, D2YellowEH, D2PinkEH, D2BlueEH).get(i));

            studyHallD0.put(TokenEnum.values()[i], Arrays.asList(GreenSHD0, RedSHD0, YellowSHD0, PinkSHD0, BlueSHD0).get(i));
            studyHallD1.put(TokenEnum.values()[i], Arrays.asList(GreenSHD1, RedSHD1, YellowSHD1, PinkSHD1, BlueSHD1).get(i));
            studyHallD2.put(TokenEnum.values()[i], Arrays.asList(GreenSHD2, RedSHD2, YellowSHD2, PinkSHD2, BlueSHD2).get(i));
        }

        for(int i = 1; i < orderedPlayerIDs.size(); i++){
            labelUsernames.put(orderedPlayerIDs.get(i), Arrays.asList(DUsername0, DUsername1, DUsername2).get(i - 1));

            entranceHallsDImage.put(orderedPlayerIDs.get(i), Arrays.asList(entranceHallD0Image, entranceHallD1Image, entranceHallD2Image).get(i - 1));
            entranceHallsD.put(orderedPlayerIDs.get(i), Arrays.asList(entranceHallD0, entranceHallD1, entranceHallD2).get(i - 1));
            studyHallsD.put(orderedPlayerIDs.get(i), Arrays.asList(studyHallD0, studyHallD1, studyHallD2).get(i - 1));


            towerNumberD.put(orderedPlayerIDs.get(i), Arrays.asList(TowersD0, TowersD1, TowersD2).get(i - 1));
            towersD.put(orderedPlayerIDs.get(i), Arrays.asList(TowerImageD0, TowerImageD1, TowerImageD2).get(i - 1));
        }
        //*************END*************

        //*************Islands*************
        for(int i = 0; i < 5; i++){
            island0.put(i, Arrays.asList(I0GT, I0RT, I0YT, I0PT, I0BT).get(i));
            island1.put(i, Arrays.asList(I1GT, I1RT, I1YT, I1PT, I1BT).get(i));
            island2.put(i, Arrays.asList(I2GT, I2RT, I2YT, I2PT, I2BT).get(i));
            island3.put(i, Arrays.asList(I3GT, I3RT, I3YT, I3PT, I3BT).get(i));
            island4.put(i, Arrays.asList(I4GT, I4RT, I4YT, I4PT, I4BT).get(i));
            island5.put(i, Arrays.asList(I5GT, I5RT, I5YT, I5PT, I5BT).get(i));
            island6.put(i, Arrays.asList(I6GT, I6RT, I6YT, I6PT, I6BT).get(i));
            island7.put(i, Arrays.asList(I7GT, I7RT, I7YT, I7PT, I7BT).get(i));
            island8.put(i, Arrays.asList(I8GT, I8RT, I8YT, I8PT, I8BT).get(i));
            island9.put(i, Arrays.asList(I9GT, I9RT, I9YT, I9PT, I9BT).get(i));
            island10.put(i, Arrays.asList(I10GT, I10RT, I10YT, I10PT, I10BT).get(i));
            island11.put(i, Arrays.asList(I11GT, I11RT, I11YT, I11PT, I11BT).get(i));
        }

        for(int i = 0; i < 12; i++){
            islandsStackPane.put(i, Arrays.asList(IStackPaneZero, IStackPaneOne, IStackPaneTwo, IStackPaneThree,
                    IStackPaneFour, IStackPaneFive, IStackPaneSix, IStackPaneSeven, IStackPaneEight, IStackPaneNine, IStackPaneTen, IStackPaneEleven).get(i));

            islandBackgroundImage.put(i, Arrays.asList(IslandZero, IslandOne, IslandTwo, IslandThree,
                    IslandFour, IslandFive, IslandSix, IslandSeven, IslandEight, IslandNine,
                    IslandTen, IslandEleven).get(i));

            islandIndexLabel.put(i, Arrays.asList(IslandIndexLabel0, IslandIndexLabel1, IslandIndexLabel2, IslandIndexLabel3,
                    IslandIndexLabel4, IslandIndexLabel5, IslandIndexLabel6, IslandIndexLabel7, IslandIndexLabel8, IslandIndexLabel9,
                    IslandIndexLabel10, IslandIndexLabel11).get(i));

            islandsLabel.put(i, Arrays.asList(island0, island1, island2, island3, island4, island5, island6, island7, island8,
                    island9, island10, island11).get(i));

            motherNatureImages.put(i, Arrays.asList(MNZero, MNOne, MNTwo, MNThree, MNFour, MNFive, MNSix, MNSeven, MNEight, MNNine, MNTen, MNEleven).get(i));

            towerIslandsImages.put(i, Arrays.asList(TowerZero, TowerOne, TowerTwo, TowerThree,
                    TowerFour, TowerFive, TowerSix, TowerSeven, TowerEight, TowerNine, TowerTen, TowerEleven).get(i));
        }
        //*************END*************

        //*************Clouds*************
        cloudsStackPane.addAll(Arrays.asList(CStackPaneZero, CStackPaneOne, CStackPaneTwo, CStackPaneThree));
        cloud0.addAll(Arrays.asList(TokenZeroCloudZero, TokenOneCloudZero, TokenTwoCloudZero, TokenThreeCloudZero));
        cloud1.addAll(Arrays.asList(TokenZeroCloudOne, TokenOneCloudOne, TokenTwoCloudOne, TokenThreeCloudOne));
        cloud2.addAll(Arrays.asList(TokenZeroCloudTwo, TokenOneCloudTwo, TokenTwoCloudTwo, TokenThreeCloudTwo));
        cloud3.addAll(Arrays.asList(TokenZeroCloudThree, TokenOneCloudThree, TokenTwoCloudThree));
        clouds.addAll(Arrays.asList(cloud0, cloud1, cloud2, cloud3));
        //*************END*************

        //*************Card*************
        paneAssistantCards.addAll(Arrays.asList(PASC0, PASC1, PASC2, PASC3, PASC4, PASC5, PASC6, PASC7, PASC8, PASC9));

        for (int i = 0; i < properties.getGameDTO().getCharacterCards().size(); i++) {
            ccCardsByName.put(properties.getGameDTO().getCharacterCards().get(i).getCardName(), Arrays.asList(CharacterCardZero, CharacterCardOne, CharacterCardTwo).get(i));
            ccCardCoinsByName.put(properties.getGameDTO().getCharacterCards().get(i).getCardName(), Arrays.asList(CharacterCardZeroCoin, CharacterCardOneCoin, CharacterCardTwoCoin).get(i));
        }
        card1Tokens.addAll(Arrays.asList(Card1Token0, Card1Token1, Card1Token2, Card1Token3));
        card7Tokens.addAll(Arrays.asList(Card7T0, Card7T1, Card7T2, Card7T3, Card7T4, Card7T5));
        card7EHTokens.addAll(Arrays.asList(Card7EHT0, Card7EHT1, Card7EHT2, Card7EHT3, Card7EHT4, Card7EHT5, Card7EHT6, Card7EHT7, Card7EHT8));
        card11Tokens.addAll(Arrays.asList(Card11T0, Card11T1, Card11T2, Card11T3));
        //*************END*************

        for (Label label : Arrays.asList(LobbyStandByP0, LobbyStandByP1, LobbyStandByP2, LobbyStandByP3))
            lobbyStandByPs.add(label);

        for(int i = 0; i < orderedPlayerIDs.size(); i++){
            playedASCsMapByUsername.put(orderedPlayerIDs.get(i), Arrays.asList(PlayedASC, PlayedACD0, PlayedACD1, PlayedACD2).get(i));
            coinsD.put(orderedPlayerIDs.get(i), Arrays.asList(CoinLabel, CoinsD0, CoinsD1, CoinsD2).get(i));
        }
    }

    private void setUpNormalGame() {
        CoinImage.setOpacity(0);
        CoinImage.setDisable(true);

        for(Label label : Arrays.asList(CoinLabel, CoinsD0, CoinsD1, CoinsD2)){
            label.setOpacity(0);
            label.setDisable(true);
        }

        CharacterCardsBox.setOpacity(0);
        CharacterCardsBox.setDisable(true);
    }
    private void setUpExpertGame() {
        readCharacterCards();
    }
    private void setUpTwoPlayers() {
        CStackPaneThree.setOpacity(0);
        CStackPaneThree.setDisable(true);

        CStackPaneTwo.setOpacity(0);
        CStackPaneTwo.setDisable(true);

        DStackPane1.setOpacity(0);
        DStackPane1.setDisable(true);
        DUsername1.setOpacity(0);
        DUsername1.setDisable(true);

        DStackPane2.setOpacity(0);
        DStackPane2.setDisable(true);
        DUsername2.setOpacity(0);
        DUsername2.setDisable(true);
    }
    private void setUpThreePlayers() {
        CStackPaneThree.setOpacity(0);
        CStackPaneThree.setDisable(true);

        DStackPane2.setOpacity(0);
        DStackPane2.setDisable(true);
        DUsername2.setOpacity(0);
        DUsername2.setDisable(true);
    }
    //*******************END*******************

    //*******************Object Reads*******************
    private void readPlayersDashboard() {
        Map<String, DashboardDTO> dashboards = properties.getGameDTO().getDashboards();
        List<String> players = properties.getPlayers();

        for (String player : players) {

            readPlayerEntranceHall(player);
            readPlayerStudyHall(player);
            readPlayerProfessors(player);

            if(!player.equals(properties.getUsername())){
                labelUsernames.get(player).setText(player);
                towersD.get(player).setImage(new Image(getTowerImagePathFromColor(dashboards.get(player).getTowerColour())));
                towerNumberD.get(player).setText(dashboards.get(player).getTowers().toString());
            }else{
                readTowers();
            }

            coinsD.get(player).setText(dashboards.get(player).getCoins().toString());
            if (dashboards.get(player).getPlayedCards().size() > 0)
                playedASCsMapByUsername.get(player).setImage(new Image(getASCImagePathFromID(dashboards.get(player).getPlayedCards().peek())));
        }
    }
    private void readPlayerEntranceHall(String playerID) {
        if (playerID.equals(properties.getUsername())) {
            int shift = 0;

            for (TokenEnum token : TokenEnum.values()) {
                for (int i = 0; i < properties.getGameDTO().getDashboards().get(properties.getUsername()).getEntranceHall().get(token); i++)
                    myEntranceHall.get(shift + i).setImage(new Image(getTokenImagePathFromColor(token)));
                shift += properties.getGameDTO().getDashboards().get(properties.getUsername()).getEntranceHall().get(token);
            }
            for (int i = shift; i < myEntranceHall.size(); i++)
                myEntranceHall.get(i).setImage(null);
        } else {
            Map<String, DashboardDTO> dashboards = properties.getGameDTO().getDashboards();

            for (TokenEnum token : TokenEnum.values())
                entranceHallsD.get(playerID).get(token).setText(dashboards.get(playerID).getEntranceHall().get(token).toString());
        }
    }
    private void readPlayerStudyHall(String playerID) {
        if (playerID.equals(properties.getUsername())) {
            for (TokenEnum color : TokenEnum.values())
                for (int i = 0; i < properties.getGameDTO().getDashboards().get(properties.getUsername()).getStudyHall().get(color); i++)
                    myStudyHallByTokenColor.get(color).get(i).setImage(new Image(getTokenImagePathFromColor(color)));
        } else {
            Map<String, DashboardDTO> dashboards = properties.getGameDTO().getDashboards();
            for (TokenEnum token : TokenEnum.values()) {
                studyHallsD.get(playerID).get(token)
                        .setText(dashboards.get(playerID).getStudyHall().get(token).toString());
            }
        }
    }
    private void readPlayerProfessors(String playerID) {
        if (playerID.equals(properties.getUsername())) {
            for(TokenEnum token : TokenEnum.values()){
                if(properties.getGameDTO().getDashboards().get(playerID).getProfessors().contains(token))
                    myProfessorHall.get(token).setImage(new Image(getProfImagePathFromColor(token)));
                else
                    myProfessorHall.get(token).setImage(null);
            }
        } else {
            for (TokenEnum token : TokenEnum.values())
                if(properties.getGameDTO().getDashboards().get(playerID).getProfessors().contains(token))
                    studyHallsD.get(playerID).get(token).setTextFill(Color.color((double)249/255, (double)166/255, (double)2/255));
                else
                    studyHallsD.get(playerID).get(token).setTextFill(Color.color(1, 1, 1));
        }
    }
    private void readTowers() {
        int i = 0;
        for (i = 0; i < properties.getGameDTO().getDashboards().get(properties.getUsername()).getTowers(); i++) {
            myTowerHall.get(i).setImage(new Image(getTowerImagePathFromColor(properties.getGameDTO().getDashboards().get(properties.getUsername()).getTowerColour())));
        }
        for (; i < 8; i++)
            myTowerHall.get(i).setImage(null);
    }
    private void readIslands() {
        int i;
        for (i = 0; i < properties.getGameDTO().getIslands().size(); i++) {
            IslandDTO island = properties.getGameDTO().getIslands().get(i);

            //Sets the value of the labels for each token
            for (TokenEnum token : TokenEnum.values())
                islandsLabel.get(i).get(Arrays.stream(TokenEnum.values()).toList().indexOf(token)).setText(island.getTokens().get(token).toString());

            islandIndexLabel.get(i).setText(Integer.toString(i));
            Position pos = MatheMagic.islandPosition(new Position(470.0, 470.0), 432.0, i, properties.getGameDTO().getIslands().size(), 0.0);
            islandsStackPane.get(i).setLayoutX(pos.x());
            islandsStackPane.get(i).setLayoutY(pos.y());

            islandBackgroundImage.get(i).setImage(new Image(getIslandImageFromIslandSize(island.getSize())));

            //Sets MotherNature
            if (island.hasMotherNature())
                motherNatureImages.get(i).setImage(new Image(getClass().getClassLoader().getResource("resources/tokens_towers/mother_nature.png").toString()));
            else
                motherNatureImages.get(i).setImage(null);

            //Sets the towers
            if (!island.getTowerColour().equals(TowerEnum.NONE))
                towerIslandsImages.get(i).setImage(new Image(getTowerImagePathFromColor(island.getTowerColour())));
            else
                towerIslandsImages.get(i).setImage(null);
        }
        for (; i < 12; i++){
            islandsStackPane.get(i).setDisable(true);
            islandsStackPane.get(i).setOpacity(0);
        }
    }
    private void readTowersOnIslands() {
        for (int i = 0; i < properties.getGameDTO().getIslands().size(); i++) {
            IslandDTO island = properties.getGameDTO().getIslands().get(i);
            if (!island.getTowerColour().equals(TowerEnum.NONE))
                towerIslandsImages.get(i).setImage(new Image(getTowerImagePathFromColor(island.getTowerColour())));
        }
    }
    private void readClouds() {
        for (int j = 0; j < properties.getGameDTO().getClouds().size(); j++) {
            int shift = 0;

            for (TokenEnum token : TokenEnum.values()) {
                for (int i = 0; i < properties.getGameDTO().getClouds().get(j).get(token); i++)
                    clouds.get(j).get(shift + i).setImage(new Image(getTokenImagePathFromColor(token)));
                shift += properties.getGameDTO().getClouds().get(j).get(token);
            }
        }
    }
    private void readAssistantCards() {
        List<Integer> played = properties.getGameDTO().getDashboards().get(properties.getUsername()).getPlayedCards().stream().toList();
        for (int i : played) {
            paneAssistantCards.get(i - 1).setDisable(true);
            paneAssistantCards.get(i - 1).setOpacity(0);
        }
    }
    private void readCharacterCards() {
        List<CharacterCardDTO> cards = properties.getGameDTO().getCharacterCards();

        if (properties.getGameDTO().getCharacterCards().size() == 3) {

            for(String cardName : properties.getGameDTO().getCharacterCards().stream().map(x -> x.getCardName()).toList())
                ccCardsByName.get(cardName).setImage(new Image(getCharacterCardImagePathFromName(cardName)));


            CoinLabel.setText(properties.getGameDTO().getDashboards().get(properties.getUsername()).getCoins().toString());

            for (CharacterCardDTO card : cards) {
                switch (card.getCardName()) {
                    case "Card1":
                        readCard1(card);
                        break;
                    case "Card7":
                        readCard7(card);
                        break;
                    case "Card11":
                        readCard11(card);
                        break;
                }
            }

        } else
            throw new RuntimeException("There are not 3 character cards");
    }
    private void readCard1(CharacterCardDTO card) {
        readCollImageView(card.getTokens(), card1Tokens);
    }
    private void readCard7(CharacterCardDTO card) {
        readCollImageView(card.getTokens(), card7Tokens);
        readCollImageView(properties.getGameDTO().getDashboards().get(properties.getUsername()).getEntranceHall(), card7EHTokens);
        for(int i = properties.getGameDTO().getDashboards().get(properties.getUsername()).getEntranceHall().values().stream().mapToInt(x -> x).sum(); i < 9; i++){
            card7EHTokens.get(i).setImage(null);
            card7EHTokens.get(i).setDisable(true);
        }
    }
    private void readCard11(CharacterCardDTO card) {
        readCollImageView(card.getTokens(), card11Tokens);
    }

    private void readCollImageView(Map<TokenEnum, Integer> coll, List<ImageView> listImg) {
        int shift = 0;
        for (TokenEnum token : TokenEnum.values()) {
            for(int i = 0; i < coll.get(token); i++){
                listImg.get(shift + i).setDisable(false);
                listImg.get(shift + i).setImage(new Image(getTokenImagePathFromColor(token)));
            }
            shift += coll.get(token);
        }
    }
    //*******************END*******************

    //*******************Utils*******************
    private String getTokenImagePathFromColor(TokenEnum token) {
        return switch (token) {
            case GREEN ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/student_green.png").toString();
            case RED -> getClass().getClassLoader().getResource("resources/tokens_towers/student_red.png").toString();
            case YELLOW ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/student_yellow.png").toString();
            case VIOLET ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/student_pink.png").toString();
            case BLUE -> getClass().getClassLoader().getResource("resources/tokens_towers/student_blue.png").toString();
        };
    }
    private String getProfImagePathFromColor(TokenEnum token) {
        return switch (token) {
            case GREEN ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/teacher_green.png").toString();
            case RED -> getClass().getClassLoader().getResource("resources/tokens_towers/teacher_red.png").toString();
            case YELLOW ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/teacher_yellow.png").toString();
            case VIOLET ->
                    getClass().getClassLoader().getResource("resources/tokens_towers/teacher_pink.png").toString();
            case BLUE -> getClass().getClassLoader().getResource("resources/tokens_towers/teacher_blue.png").toString();
        };
    }
    private String getTowerImagePathFromColor(TowerEnum color) {
        return switch (color) {
            case WHITE -> getClass().getClassLoader().getResource("resources/tokens_towers/tower_white.png").toString();
            case BLACK -> getClass().getClassLoader().getResource("resources/tokens_towers/tower_black.png").toString();
            case GREY -> getClass().getClassLoader().getResource("resources/tokens_towers/tower_grey.png").toString();
            default -> null;
        };
    }
    private String getCharacterCardImagePathFromName(String name) {
        return switch (name) {
            case "Card1" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard1.png").toString();
            case "Card2" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard2.png").toString();
            case "Card4" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard4.png").toString();
            case "Card6" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard6.png").toString();
            case "Card7" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard7.png").toString();
            case "Card8" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard8.png").toString();
            case "Card9" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard9.png").toString();
            case "Card11" ->
                    getClass().getClassLoader().getResource("resources/characterCards/FrontCard11.png").toString();
            default -> null;
        };
    }
    private String getMagicianImagePathFromName(String name) {
        return switch (name) {
            case "WizardMagician" ->
                    getClass().getClassLoader().getResource("resources/magicians/Magician1.png").toString();
            case "KingMagician" ->
                    getClass().getClassLoader().getResource("resources/magicians/Magician2.png").toString();
            case "WitchMagician" ->
                    getClass().getClassLoader().getResource("resources/magicians/Magician3.png").toString();
            case "MonkMagician" ->
                    getClass().getClassLoader().getResource("resources/magicians/Magician4.png").toString();
            default -> null;
        };
    }
    private String getASCImagePathFromID(int card) {
        return switch (card) {
            case 1 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant1.png").toString();
            case 2 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant2.png").toString();
            case 3 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant3.png").toString();
            case 4 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant4.png").toString();
            case 5 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant5.png").toString();
            case 6 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant6.png").toString();
            case 7 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant7.png").toString();
            case 8 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant8.png").toString();
            case 9 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant9.png").toString();
            case 10 ->
                    getClass().getClassLoader().getResource("resources/assistant_cards/FrontAssistant10.png").toString();
            default -> null;
        };
    }
    private TokenEnum getTokenColorFromImgPath(String path) {
        for (TokenEnum token : TokenEnum.values()) {
            if (path.equals(getTokenImagePathFromColor(token))) {
                return token;
            }
        }
        throw new RuntimeException("it wasn't possible to find the color of the token from the image path");
    }
    private String getCCNameFromPath(String url) {
        List<String> names = new ArrayList<>();
        for (String name : Arrays.asList("Card1", "Card2", "Card4", "Card6", "Card7", "Card8", "Card9", "Card11"))
            names.add(name);
        for (String name : names)
            if (url.equals(getCharacterCardImagePathFromName(name)))
                return name;
        throw new RuntimeException("It was not possible to find the name of the character card.");
    }
    private String getIslandImageFromIslandSize(int size){
        if(size < 1 || size > 12)
            throw new RuntimeException("This size of the island you gave is :" + size);
        return switch (size){
            case 1-> getClass().getClassLoader().getResource("resources/islands/island1.png").toString();
            case 2-> getClass().getClassLoader().getResource("resources/islands/merge2.png").toString();
            case 3-> getClass().getClassLoader().getResource("resources/islands/merge3.png").toString();
            case 4-> getClass().getClassLoader().getResource("resources/islands/merge4.png").toString();
            case 5-> getClass().getClassLoader().getResource("resources/islands/merge5.png").toString();
            case 6-> getClass().getClassLoader().getResource("resources/islands/merge6.png").toString();
            case 7-> getClass().getClassLoader().getResource("resources/islands/merge7.png").toString();
            case 8-> getClass().getClassLoader().getResource("resources/islands/merge8.png").toString();
            case 9-> getClass().getClassLoader().getResource("resources/islands/merge9.png").toString();
            default -> getClass().getClassLoader().getResource("resources/islands/merge10.png").toString();
        };
    }
    private boolean checkTurn() {
        if (!properties.getGameDTO().getCurrentPlayer().equals(properties.getUsername())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("It is " + properties.getGameDTO().getCurrentPlayer() + " turn\n");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean checkCharacterCardStatus() {
        if (properties.getGameDTO().getGamePhase().equals("PlayApprenticeCardState")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot play a character card now.\n");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private void resetCardVariables(){
        for (ImageView image : card7ToAdd)
            image.setEffect(null);
        for (ImageView image : card7ToRemove)
            image.setEffect(null);
        card7ToRemove = new ArrayList<>();
        card7ToAdd = new ArrayList<>();
        if (card1ChosenToken != null)
            card1ChosenToken.setEffect(null);
        card1ChosenToken = null;
    }
    private void changeCurrentPlayerIndicator(String currentPlayer, String currentState) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(80);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        playersDashboardImages.get(currentPlayer).setEffect(borderGlow);

        if (currentPlayer.equals(properties.getUsername())) {
            this.PlayerTurnIndicator.setText("It is your turn.\n" + getIndicatorByState(0 ,currentState));
            return;
        }
        this.PlayerTurnIndicator.setText("It is " + currentPlayer + "'s turn.\n" + getIndicatorByState(1, currentState));
    }

    private String getIndicatorByState(int flag, String currentState){
        switch (currentState){
            case "PlayApprenticeCardState":
                if(flag == 0)
                    return "You have to play an apprentice card";
                return "The player has to plan an apprentice card";
            case "PlaceTokensState":
                if(flag == 0)
                    return "You have to place your tokens";
                return "The player has to place the tokens";
            case "PickACloudState":
                if(flag == 0)
                    return "You have to pick a cloud";
                return "The player has to pick a cloud";
            case "MoveMotherNatureState":
                if(flag == 0)
                    return "You have to move mother nature";
                return "The player has to more mother nature";
            case "GameOverState":
                return "Game Over";
        }
        return "";
    }

    private int getLayoutXByUser(String user){
        switch (orderedPlayerIDs.indexOf(user)){
            case 0:
                return 1387;
            default:
                return 1423;
        }
    }

    private int getLayoutYByUser(String user){
        switch (orderedPlayerIDs.indexOf(user)){
            case 0:
                return 924;
            case 1:
                return 130;
            case 2:
                return 305;
            case 3:
                return 492;
        }
        return -1;
    }
    //*******************END*******************

    //*******************Object Events*******************
    public void CharacterCardClicked(MouseEvent mouseEvent) throws Exception {
        String CCName = getCCNameFromPath(((ImageView)((StackPane) mouseEvent.getSource()).getChildren().get(0)).getImage().getUrl());

        if (List.of("Card2", "Card4", "Card6", "Card8").contains(CCName)) {
            if (!checkTurn())
                return;
            if (!checkCharacterCardStatus())
                return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Do you want to active the character card?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                properties.getConnector().sendRequest(new PlayCharacterCardReq(CCName));
            }
            return;
        }

        Pane pane;
        switch (CCName) {
            case "Card1":
                pane = Card1Pane;
                break;
            case "Card7":
                pane = Card7Pane;
                break;
            case "Card9":
                pane = Card9Pane;
                break;
            case "Card11":
                pane = Card11Pane;
                break;
            default:
                throw new RuntimeException("There has been an error when find the name of the character card");
        }

        pane.setDisable(false);

        FadeTransition fade = new FadeTransition();
        fade.setNode(pane);
        fade.setDuration(Duration.millis(1000));
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(pane.getOpacity());
        fade.setToValue(1);
        fade.play();

        pane.setOpacity(1);
    }
    public void CharacterCardEntered(MouseEvent mouseEvent) {
        oldCardEffect = ((ImageView)((StackPane) mouseEvent.getSource()).getChildren().get(0)).getEffect();

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(40);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        ((ImageView)((StackPane) mouseEvent.getSource()).getChildren().get(0)).setEffect(borderGlow);

    }
    public void CharacterCardExited(MouseEvent mouseEvent) {
        ((ImageView)((StackPane) mouseEvent.getSource()).getChildren().get(0)).setEffect(oldCardEffect);
        ((ImageView)((StackPane) mouseEvent.getSource()).getChildren().get(0)).setOpacity(1);
    }

    public void ASCsClicked(MouseEvent mouseEvent) {
        FadeTransition fade = new FadeTransition();
        fade.setNode(ASCPane);
        fade.setDuration(Duration.millis(1000));
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(ASCPane.getOpacity());
        fade.setToValue(1);
        fade.play();

        ASCPane.setDisable(false);
        ASCPane.setOpacity(1);
    }
    public void ASCsEntered(MouseEvent mouseEvent) {

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(40);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        ((ImageView) mouseEvent.getSource()).setEffect(borderGlow);
    }
    public void ASCsExited(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setEffect(null);
    }

    public void ASCPClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;

        int ID = paneAssistantCards.indexOf(mouseEvent.getSource());

        if (!properties.getGameDTO().getGamePhase().equals("PlayApprenticeCardState")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot play an assistant card now.\n");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to play the apprentice card: Card " + (ID + 1));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            properties.getConnector().sendRequest(new PlayApprenticeCardReq(ID + 1));

            ASCPane.setDisable(true);

            FadeTransition fade = new FadeTransition();
            fade.setNode(ASCPane);
            fade.setDuration(Duration.millis(1000));
            fade.setInterpolator(Interpolator.LINEAR);
            fade.setFromValue(ASCPane.getOpacity());
            fade.setToValue(0);
            fade.play();

            ASCPane.setOpacity(0);
        }

    }
    public void ASCPEntered(MouseEvent mouseEvent) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(40);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        ((ImageView) mouseEvent.getSource()).setEffect(borderGlow);
    }
    public void ASCPExited(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setEffect(null);
    }

    public void PaneCloseButtonClicked(ActionEvent actionEvent) {
        resetCardVariables();

        Parent pane = ((Button) (actionEvent.getSource())).getParent();
        pane.setDisable(true);

        FadeTransition fade = new FadeTransition();
        fade.setNode(pane);
        fade.setDuration(Duration.millis(1000));
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(pane.getOpacity());
        fade.setToValue(0);
        fade.play();

        pane.setOpacity(0);
    }

    public void EntranceHallClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!properties.getGameDTO().getGamePhase().equals("PlaceTokensState")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot pick a token now.\n");
            alert.showAndWait();
            return;
        }
        if (((ImageView) mouseEvent.getSource()).getImage() == null) {
            return;
        }

        if (entranceHallChosenStudent == null) {
            entranceHallChosenStudent = (ImageView) mouseEvent.getSource();
            Glow glow = new Glow();
            glow.setLevel(10);
            entranceHallChosenStudent.setEffect(glow);
        } else {
            entranceHallChosenStudent.setEffect(null);
            if (entranceHallChosenStudent.equals((ImageView) mouseEvent.getSource())) {
                entranceHallChosenStudent = null;
                return;
            }
            entranceHallChosenStudent = (ImageView) mouseEvent.getSource();
            Glow glow = new Glow();
            glow.setLevel(10);
            entranceHallChosenStudent.setEffect(glow);
        }
    }
    public void IslandClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;

        if (properties.getGameDTO().getGamePhase().equals("PlaceTokensState")) {
            if (entranceHallChosenStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You have to choose a student first.\n");
                alert.showAndWait();
                return;
            }
            entranceHallChosenStudent.setEffect(null);
            properties.getConnector().sendRequest(new MoveToIslandReq(getTokenColorFromImgPath(entranceHallChosenStudent.getImage().getUrl()),
                    islandsStackPane.values().stream().toList().indexOf(mouseEvent.getSource())));
            entranceHallChosenStudent = null;
        } else if (properties.getGameDTO().getGamePhase().equals("MoveMotherNatureState")) {
            int motherNatureIndex = 0;
            for (IslandDTO islandDTO : properties.getGameDTO().getIslands()) {
                if (islandDTO.hasMotherNature())
                    motherNatureIndex = properties.getGameDTO().getIslands().indexOf(islandDTO);
            }

            int movementValue = islandsStackPane.values().stream().toList().indexOf(mouseEvent.getSource()) - motherNatureIndex;
            properties.getConnector().sendRequest(new MoveMotherNatureReq((movementValue > 0 ? movementValue : movementValue +
                    properties.getGameDTO().getIslands().size())));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot interact with the islands now.\n");
            alert.showAndWait();
        }
    }

    public void MouseEnteredIsland(MouseEvent mouseEvent) {
        Integer index = islandsStackPane.values().stream().toList().indexOf((StackPane) mouseEvent.getSource());

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        islandBackgroundImage.get(index).setEffect(borderGlow);
        islandsStackPane.get(index).setOpacity(0.6);
    }

    public void MouseExitedIsland(MouseEvent mouseEvent) {
        Integer index = islandsStackPane.values().stream().toList().indexOf((StackPane) mouseEvent.getSource());
        islandBackgroundImage.get(index).setEffect(null);
        islandsStackPane.get(index).setOpacity(1);
    }

    public void StudyHallClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;

        if (properties.getGameDTO().getGamePhase().equals("PlaceTokensState")) {
            if (entranceHallChosenStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You have to choose a student first.\n");
                alert.showAndWait();
                return;
            }
            entranceHallChosenStudent.setEffect(null);
            properties.getConnector().sendRequest(new MoveToStudyHallReq(getTokenColorFromImgPath(entranceHallChosenStudent.getImage().getUrl())));
            entranceHallChosenStudent = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot place a token now.\n");
            alert.showAndWait();
        }
    }
    public void CloudClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;

        if (properties.getGameDTO().getGamePhase().equals("PickACloudState")) {
            properties.getConnector().sendRequest(new PickCloudReq(cloudsStackPane.indexOf(mouseEvent.getSource())));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot pick a cloud now now.\n");
            alert.showAndWait();
        }
    }

    public void CloudMouseEntered(MouseEvent mouseEvent) {

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        ((StackPane)mouseEvent.getSource()).getChildren().get(0).setEffect(borderGlow);
        ((StackPane)mouseEvent.getSource()).setOpacity(0.6);
    }
    public void CloudMouseExited(MouseEvent mouseEvent) {
        ((StackPane)mouseEvent.getSource()).getChildren().get(0).setEffect(null);
        ((StackPane)mouseEvent.getSource()).setOpacity(1);
    }

    public void Card1TokenClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;

        Glow glow = new Glow();
        glow.setLevel(10);

        if (card1ChosenToken == null) {
            card1ChosenToken = (ImageView) mouseEvent.getSource();
            card1ChosenToken.setEffect(glow);
        } else {
            card1ChosenToken.setEffect(null);
            if (card1ChosenToken == (ImageView) mouseEvent.getSource())
                card1ChosenToken = null;
            else {
                card1ChosenToken = (ImageView) mouseEvent.getSource();
                card1ChosenToken.setEffect(glow);
            }
        }
    }
    public void Card1ActivateButtonClicked(ActionEvent actionEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;
        if (card1ChosenToken == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please choose a token first.\n");
            alert.showAndWait();
            return;
        }
        if (Card1ChoiceBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please the island where to place the token.\n");
            alert.showAndWait();
            return;
        }

        properties.getConnector().sendRequest(new PlayCharacterCardReq("Card1", getTokenColorFromImgPath(card1ChosenToken.getImage().getUrl()), Card1ChoiceBox.getValue()));
    }

    public void Card7TClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;

        ImageView tokenImg = (ImageView) mouseEvent.getSource();
        if (card7ToAdd.contains(tokenImg)) {
            card7ToAdd.remove(tokenImg);
            tokenImg.setEffect(null);
        } else {
            if (card7ToAdd.size() == 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You can select at most 3 tokens to add. Please deselect a token.\n");
                alert.showAndWait();
                return;
            } else {
                card7ToAdd.add(tokenImg);

                Glow glow = new Glow();
                glow.setLevel(10);
                tokenImg.setEffect(glow);
            }
        }
    }
    public void Card7EHClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;

        ImageView tokenImg = (ImageView) mouseEvent.getSource();
        if (card7ToRemove.contains(tokenImg)) {
            card7ToRemove.remove(tokenImg);
            tokenImg.setEffect(null);
        } else {
            if (card7ToRemove.size() == 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You can select at most 3 tokens to remove. Please deselect a token.\n");
                alert.showAndWait();
                return;
            } else {
                card7ToRemove.add(tokenImg);

                Glow glow = new Glow();
                glow.setLevel(10);
                tokenImg.setEffect(glow);
            }
        }
    }
    public void Card7ActivateButtonClicked(ActionEvent actionEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;
        if (card7ToRemove.size() != card7ToAdd.size()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You have to select the same amount of tokens for the exchange.\n");
            alert.showAndWait();
            return;
        }
        if (card7ToRemove.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You have to select al least 1 token.\n");
            alert.showAndWait();
            return;
        }
        Map<TokenEnum, Integer> toAddMap = new HashMap<>();
        Map<TokenEnum, Integer> toRemoveMap = new HashMap<>();

        for (TokenEnum token : TokenEnum.values()) {
            int i = 0;
            for (int j = 0; j < card7ToAdd.size(); j++) {
                if (token.equals(getTokenColorFromImgPath(card7ToAdd.get(j).getImage().getUrl())))
                    i++;
            }
            toAddMap.put(token, i);
        }

        for (TokenEnum token : TokenEnum.values()) {
            int i = 0;
            for (int j = 0; j < card7ToRemove.size(); j++) {
                if (token.equals(getTokenColorFromImgPath(card7ToRemove.get(j).getImage().getUrl())))
                    i++;
            }
            toRemoveMap.put(token, i);
        }

        properties.getConnector().sendRequest(new PlayCharacterCardReq("Card7", toAddMap, toRemoveMap));
    }
    public void Card9TClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;

        TokenEnum token = getTokenColorFromImgPath(((ImageView) mouseEvent.getSource()).getImage().getUrl());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to activate Card 9 with the token: " + token);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            properties.getConnector().sendRequest(new PlayCharacterCardReq("Card9", token));
        }
    }
    public void Card11TClicked(MouseEvent mouseEvent) {
        if (!checkTurn())
            return;
        if (!checkCharacterCardStatus())
            return;

        TokenEnum token = getTokenColorFromImgPath(((ImageView) mouseEvent.getSource()).getImage().getUrl());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to activate Card 11 and insert the token: " + token + " in your study hall");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            properties.getConnector().sendRequest(new PlayCharacterCardReq("Card11", token));
        }
    }

    public void closeGame(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to exit the game?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.out.println("Player decided to disconnect");
            exit(0);
        }
    }

    public void QuitIconEntered(MouseEvent mouseEvent) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(40);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        ((ImageView) mouseEvent.getSource()).setEffect(borderGlow);
        ((ImageView) mouseEvent.getSource()).setOpacity(0.6);
    }

    public void QuitIconExited(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setEffect(null);
        ((ImageView) mouseEvent.getSource()).setOpacity(1);
    }
    //*******************END*******************

    //*******************Server Responses*******************
    public void lobbyStandby(Map<String, ConnectionStatusEnum> playersConnections) {
        int i = 0;
        LobbyStandByPane.setOpacity(1);
        LobbyStandByPane.setDisable(false);
        for (i = 0; i < playersConnections.keySet().size(); i++) {
            lobbyStandByPs.get(i).setOpacity(1);
            lobbyStandByPs.get(i).setText(playersConnections.keySet().stream().toList().get(i) + " : "
                    + playersConnections.get(playersConnections.keySet().stream().toList().get(i)));
        }
        for (; i < 4; i++)
            lobbyStandByPs.get(i).setOpacity(0);
    }
    public void gameStatus() {
        LobbyStandByPane.setOpacity(0);
        LobbyStandByPane.setDisable(true);
        properties.setLobbyIsInStandBy(false);

    }
    public void disconnectedRed(String reason) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("You have disconnected from the server.\nReason: " + reason);
        alert.showAndWait();

        exit(0);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_files/EntryScreen.fxml"));
        properties.setFxmlLoader(fxmlLoader);

        Scene scene = new Scene(fxmlLoader.load());
        Stage primaryStage = Properties.getInstance().getPrimaryStage();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //*******************END*******************

    //*******************Game Events*******************
    public void mergedIslands(MergedIslandsEvent mergedIslandsEvent) {
        readIslands();
    }
    public void commandFailed(CommandFailedEvent commandFailedEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("There has been an error.\nMessage: " + commandFailedEvent.getErrorMessage());
        alert.showAndWait();
        return;
    }
    public void newState(NewStateGameEvent newStateGameEvent) {
        if(oldGameState.equals(newStateGameEvent.getNewStateName()))
            return;
        oldGameState = newStateGameEvent.getNewStateName();
        changeCurrentPlayerIndicator(oldCurrentPlayer, oldGameState);
        //if(newStateGameEvent.getNewStateName().equals("PlayApprenticeCardState")){
        //    for(String user : properties.getPlayers())
        //        playedASCsMapByUsername.get(user).setImage(null);
        //}

        //if (oldGameState.equals(newStateGameEvent.getNewStateName()) || !properties.getUsername().equals(properties.getGameDTO().getCurrentPlayer()))
        //    return;
        //oldGameState = newStateGameEvent.getNewStateName();
        //Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setTitle("State Changed");
        //alert.setContentText("The state of game has changed.\n" +
        //        "Current state: " + newStateGameEvent.getNewStateName());
        //alert.showAndWait();
        //return;
    }
    public void currentPlayerChanged(CurrentPlayerChangedGameEvent currentPlayerChangedGameEvent) {
        if (oldCurrentPlayer.equals(currentPlayerChangedGameEvent.getCurrentPlayer()))
            return;

        playersDashboardImages.get(oldCurrentPlayer).setEffect(null);
        oldCurrentPlayer = properties.getGameDTO().getCurrentPlayer();
        changeCurrentPlayerIndicator(oldCurrentPlayer, oldGameState);

        String msg = "It is " + currentPlayerChangedGameEvent.getCurrentPlayer() + "'s turn.";
        if (oldCurrentPlayer.equals(properties.getUsername()))
            msg = "It is your turn.";
    }
    public void bagToCloud(BagToCloudEvent bagToCloudEvent) {
        int shift = 0;
        for (TokenEnum token : TokenEnum.values()) {
            for (int i = 0; i < bagToCloudEvent.getTokens().get(token); i++)
                clouds.get(bagToCloudEvent.getCloudIndex()).get(shift + i).setImage(new Image(getTokenImagePathFromColor(token)));
            shift += bagToCloudEvent.getTokens().get(token);
        }
    }
    public void playedApprenticeCard(PlayedApprenticeCardEvent playedApprenticeCardEvent) {
        readAssistantCards();
        playedASCsMapByUsername.get(playedApprenticeCardEvent.getPlayerID()).setImage(new Image(getASCImagePathFromID(playedApprenticeCardEvent.getCardID())));
    }
    public void professorChanged(ProfessorChangedEvent professorChangedEvent) {
        for (String playerID : orderedPlayerIDs)
            readPlayerProfessors(playerID);
    }
    public void tokenToStudyHall(TokenToStudyHallEvent tokenToStudyHallEvent) {
        String playerID = tokenToStudyHallEvent.getPlayerID();
        readPlayerEntranceHall(playerID);
        readPlayerStudyHall(playerID);

        if(!properties.getGameDTO().getCharacterCards().isEmpty()){
            if(properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().contains("Card7")){
                int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
                readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex));
            }
        }
    }
    public void tokenToIsland(TokenToIslandEvent tokenToIslandEvent) {
        int islandIndex = tokenToIslandEvent.getIslandIndex();

        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(200));
        scaleTransition.setNode(islandBackgroundImage.get(islandIndex));
        scaleTransition.setByY(1);
        scaleTransition.setByX(1);
        scaleTransition.setAutoReverse(false);

        ScaleTransition scaleTransition1 = new ScaleTransition();
        scaleTransition1.setNode(islandBackgroundImage.get(islandIndex));
        scaleTransition1.setDuration(Duration.millis(200));
        scaleTransition1.setByY(-1);
        scaleTransition1.setByX(-1);
        scaleTransition1.setAutoReverse(false);

        SequentialTransition seqT = new SequentialTransition (scaleTransition, scaleTransition1);
        seqT.play();

        IslandDTO island = properties.getGameDTO().getIslands().get(islandIndex);
        for (TokenEnum token : TokenEnum.values())
            islandsLabel.get(islandIndex).get(Arrays.stream(TokenEnum.values()).toList().indexOf(token)).setText(island.getTokens().get(token).toString());

        readPlayerEntranceHall(tokenToIslandEvent.getPlayerID());

        if(!properties.getGameDTO().getCharacterCards().isEmpty()){
            if(properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().contains("Card7")){
                int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
                readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex));
            }
        }
    }

    public void cloudToEntranceHall(CloudToEntranceHallEvent cloudToEntranceHallEvent) {
        readPlayerEntranceHall(cloudToEntranceHallEvent.getReceivingPlayer());

        for (ImageView image : clouds.get(cloudToEntranceHallEvent.getCloudIndex()))
            image.setImage(null);

        if(!properties.getGameDTO().getCharacterCards().isEmpty()){
            if(properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().contains("Card7")){
                int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
                readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex));
            }
        }
    }
    public void moveMotherNature(MoveMotherNatureEvent moveMotherNatureEvent) {
        motherNatureImages.get(moveMotherNatureEvent.getFrom()).setImage(null);
        motherNatureImages.get(moveMotherNatureEvent.getTo()).setImage(new Image(getClass().getClassLoader().getResource("resources/tokens_towers/mother_nature.png").toString()));
    }
    public void towersToDashBoard(TowersToDashBoardEvent towersToDashBoardEvent) {
        updatePlayersTowers(towersToDashBoardEvent.getPlayerID());
        readTowersOnIslands();
    }
    public void towersToIsland(TowersToIslandEvent towersToIslandEvent) {
        towerIslandsImages.get(towersToIslandEvent.getIsland()).setImage(new Image(getTowerImagePathFromColor(towersToIslandEvent.getTower())));
        updatePlayersTowers(towersToIslandEvent.getPlayerID());
    }
    public void updatePlayersTowers(String playerID) {
        Map<String, DashboardDTO> dashboards = properties.getGameDTO().getDashboards();

        if (playerID.equals(properties.getUsername()))
            readTowers();
        else {
            towerNumberD.get(playerID)
                    .setText(dashboards.get(playerID).getTowers().toString());
        }
    }
    public void gameIsOver(GameIsOverEvent gameIsOverEvent) {
        GameIsOverPane.setOpacity(1);
        GameIsOverPane.setDisable(false);

        StringBuilder winnersString = new StringBuilder("The winners are: ");
        for(String string : gameIsOverEvent.getWinner())
            winnersString.append(string).append(" ");

        GameIsOverLabel.setText(winnersString.toString());
    }
    public void ccActivated(CCActivatedEvent ccActivatedEvent) {

        for(String string : orderedPlayerIDs)
            coinsD.get(string).setText(properties.getGameDTO().getDashboards().get(string).getCoins().toString());

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.GOLD);
        borderGlow.setHeight(80);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);

        ccCardsByName.get(ccActivatedEvent.getCardName()).setEffect(borderGlow);
        ccCardCoinsByName.get(ccActivatedEvent.getCardName()).setImage(new Image(getClass().getClassLoader().getResource("resources/Coin.png").toString()));
    }
    public void ccDeActivated(CCDeActivated ccDeActivated) {
        ccCardsByName.get(ccDeActivated.getCardName()).setEffect(null);
    }
    public void cardToEntranceHall(CardToEntranceHallEvent cardToEntranceHallEvent) {
        resetCardVariables();
        for (ImageView image : card7ToAdd)
            image.setEffect(null);
        card7ToAdd = new ArrayList<>();

        int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
        readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex));
        readPlayerEntranceHall(cardToEntranceHallEvent.getPlayerID());
    }
    public void entranceHallToCard(EntranceHallToCardEvent entranceHallToCardEvent) {
        for (ImageView image : card7ToRemove)
            image.setEffect(null);
        card7ToRemove = new ArrayList<>();

        int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
        readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex));
    }
    public void cardTokenToIsland(CardTokenToIslandEvent cardTokenToIslandEvent) {

        int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card1");
        readCard1(properties.getGameDTO().getCharacterCards().get(cardIndex));
        readIslands();
    }
    public void bagToCard(BagTokenToCardEvent bagTokenToCardEvent) {
        resetCardVariables();
        switch (bagTokenToCardEvent.getCardName()) {
            case "Card1":
                int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card1");
                readCard1(properties.getGameDTO().getCharacterCards().get(cardIndex));
                break;
            case "Card7":
                int cardIndex1 = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card7");
                readCard7(properties.getGameDTO().getCharacterCards().get(cardIndex1));
                break;
            case "Card11":
                int cardIndex2 = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card11");
                readCard11(properties.getGameDTO().getCharacterCards().get(cardIndex2));
                break;
        }
    }
    public void cardTokenToStudyHall(CardTokenToStudyHallEvent cardTokenToStudyHallEvent) {
        resetCardVariables();
        int cardIndex = properties.getGameDTO().getCharacterCards().stream().map(x->x.getCardName()).toList().indexOf("Card11");
        readCard11(properties.getGameDTO().getCharacterCards().get(cardIndex));
        readPlayerStudyHall(cardTokenToStudyHallEvent.getPlayerID());
    }

    public void coinsChanged(CoinsChangedEvent coinsChangedEvent){
        coinsD.get(coinsChangedEvent.getPlayerId()).setText(Integer.toString(coinsChangedEvent.getCurrentValue()));
    }
    //*******************END*******************
}
