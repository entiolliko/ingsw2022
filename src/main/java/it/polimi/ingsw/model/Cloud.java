package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.data_transfer_objects.GameDTO;
import it.polimi.ingsw.controller.data_transfer_objects.Simplifiable;
import it.polimi.ingsw.model.custom_data_structures.TokenCollection;

import java.util.Objects;

public class Cloud implements Simplifiable {
    private final int size;
    private TokenCollection collection;

    public Cloud(int size) {
        this.size = size;
        this.collection = TokenCollection.newEmptyCollection();
    }

    /**
     * Adds a token collection to the current collection
     *
     * @param tokensToAdd The tokens to be added
     * @throws StackOverflowError Thrown in the case where the amount of tokens to be added is greater than the size of the cloud
     */
    public void addTokens(TokenCollection tokensToAdd) throws StackOverflowError {
        if (tokensToAdd.size() > this.size || tokensToAdd.size() + collection.size() > this.size)
            throw new StackOverflowError();
        collection.addToCollection(tokensToAdd);
    }

    public int size() {
        return collection.size();
    }

    /**
     * Returns the current collection and then creates a new empty collection
     *
     * @return The current collection
     */
    public TokenCollection removeTokens() {
        TokenCollection rs = collection.copy();
        collection = TokenCollection.newEmptyCollection();
        return rs;
    }
    @Override
    public String toString() {
        return "Cloud{" +
                "size=" + size +
                ", collection=" + collection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cloud cloud = (Cloud) o;
        return size == cloud.size && Objects.equals(collection, cloud.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, collection);
    }


    @Override
    public void fillDTO(GameDTO gameDTO) {
        gameDTO.getClouds().add(this.collection.getMap());
    }
}
