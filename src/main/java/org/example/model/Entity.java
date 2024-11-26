package org.example.model;

import java.io.Serializable;

/**
 * Base class for all classes to include the id
 */
public abstract class Entity implements Serializable {
    private int id;

    public Entity(int id)
    {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Id: " + id;
    }
}
