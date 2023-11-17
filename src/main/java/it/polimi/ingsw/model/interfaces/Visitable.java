package it.polimi.ingsw.model.interfaces;

import it.polimi.ingsw.controller.exceptions.ReloadGameException;
import it.polimi.ingsw.model.visitor.VisitorCommand;
import it.polimi.ingsw.custom_json_builder.Gsonable;
import org.jetbrains.annotations.NotNull;

public interface Visitable extends Gsonable {
    default void accept(@NotNull VisitorCommand concreteVisitor) throws ReloadGameException {
    }
}
