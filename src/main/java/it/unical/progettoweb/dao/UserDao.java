package it.unical.progettoweb.dao;

import it.unical.progettoweb.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<User, Integer>{
    Optional<User> findByEmail(String email);
    List<User> findAllUtenti();
    boolean existsByEmail(String email);
}
