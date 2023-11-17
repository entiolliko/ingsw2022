package it.polimi.ingsw.controller.communication_protocol.client_requests.game_commands.character_cards;

import it.polimi.ingsw.controller.communication_protocol.ClientRequest;
import it.polimi.ingsw.controller.communication_protocol.ServerAcceptor;
import it.polimi.ingsw.model.dashboard.TokenEnum;

import java.util.Map;

public class PlayCharacterCardReq extends ClientRequest {

    private final String cardName;
    private final TokenEnum token;
    private final int islandIndex;
    private final Map<TokenEnum, Integer> toRemove;
    private final Map<TokenEnum, Integer> toAdd;

    private PlayCharacterCardReq(String cardName, TokenEnum token, int islandIndex, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove) {
        this.cardName = cardName;
        this.token = token;
        this.islandIndex = islandIndex;
        this.toRemove = toRemove;
        this.toAdd = toAdd;
    }

    public PlayCharacterCardReq(String cardName) {
        this(cardName, null, -1, null, null);
    }

    public PlayCharacterCardReq(String cardName, TokenEnum token) {
        this(cardName, token, -1, null, null);
    }

    public PlayCharacterCardReq(String cardName, TokenEnum token, int islandIndex) {
        this(cardName, token, islandIndex, null, null);
    }

    public PlayCharacterCardReq(String cardName, Map<TokenEnum, Integer> toAdd, Map<TokenEnum, Integer> toRemove) {
        this(cardName, null, -1, toAdd, toRemove);
    }

    @Override
    public void visit(ServerAcceptor acceptor) {
        acceptor.playCharacterCard(cardName, token, islandIndex, toAdd, toRemove);
    }
}
