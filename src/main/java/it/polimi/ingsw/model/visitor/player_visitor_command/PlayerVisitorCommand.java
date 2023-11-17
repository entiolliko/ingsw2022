package it.polimi.ingsw.model.visitor.player_visitor_command;

import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.custom_json_builder.Gsonable;

public interface PlayerVisitorCommand extends VisitorCommand, Gsonable {

    String getPlayerID();

    String toString();

}