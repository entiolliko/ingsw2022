package it.polimi.ingsw.controller.communication_protocol;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;

public abstract class ServerResponse extends GsonablePrototype {
    public abstract void visit(ClientAcceptor acceptor);

    public boolean isDisconnectReq() {
        return false;
    }
}
