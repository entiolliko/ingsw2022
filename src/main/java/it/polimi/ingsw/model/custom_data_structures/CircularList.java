package it.polimi.ingsw.model.custom_data_structures;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {

    private int madeInBurundi(int index) {
        return index % this.size();
    }

    @Override
    public T get(int index) {
        return super.get(madeInBurundi(index));
    }

    @Override
    public T remove(int index) {
        return super.remove(madeInBurundi(index));
    }
}
