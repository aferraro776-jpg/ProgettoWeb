package it.unical.progettoweb.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {

    void save(T entity, ID id);
    Optional<T> get(ID id);
    List<T> getAll();
    void update(T entity, ID id);
    void delete(ID id);
}