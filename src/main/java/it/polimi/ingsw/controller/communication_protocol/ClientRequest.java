package it.polimi.ingsw.controller.communication_protocol;

import it.polimi.ingsw.custom_json_builder.GsonablePrototype;

public abstract class ClientRequest extends GsonablePrototype {
    public abstract void visit(ServerAcceptor acceptor);
}
