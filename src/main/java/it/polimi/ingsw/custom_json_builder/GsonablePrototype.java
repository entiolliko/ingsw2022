package it.polimi.ingsw.custom_json_builder;

import java.util.Objects;

public abstract class GsonablePrototype implements Gsonable {
    protected final String classname;

    protected GsonablePrototype() {
        classname = this.getClass().getName();
    }

    public String getClassName() {
        return this.classname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GsonablePrototype that = (GsonablePrototype) o;
        return Objects.equals(classname, that.classname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classname);
    }
}
