package com.example.laborator7.Domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observer;

public class Entity<ID> implements Serializable {
    // private static final long serialVersionUID = 7331115341259248461L;
    protected ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}