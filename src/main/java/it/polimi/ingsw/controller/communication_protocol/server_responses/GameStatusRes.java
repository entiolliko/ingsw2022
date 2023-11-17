package it.polimi.ingsw.controller.communication_protocol.server_responses;

import it.polimi.ingsw.controller.communication_protocol.ClientAcceptor;
import it.polimi.ingsw.controller.communication_protocol.ServerResponse;
import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.model.game_event.GameEvent;

import java.util.List;

public class GameStatusRes extends ServerResponse {

    private final GameDTO gameDTO;
    private final List<GameEvent> gameEventList;

    public GameStatusRes(GameDTO gameDTO, List<GameEvent> gameEventList) {
        this.gameDTO = gameDTO;
        this.gameEventList = gameEventList;
    }

    @Override
    public void visit(ClientAcceptor acceptor) {
        acceptor.gameStatus(gameDTO, gameEventList);
    }
}
