package it.polimi.ingsw.model.custom_data_structures;


import it.polimi.ingsw.model.character_cards.CharacterCard;

import java.util.ArrayList;

public class MagicList extends ArrayList<CharacterCard> {
    private final String classname = this.getClass().getName();

    public String getClassName() {
        return classname;
    }
}
