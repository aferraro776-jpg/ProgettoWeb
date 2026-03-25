package it.unical.progettoweb.dao;

import it.unical.progettoweb.model.User;

public interface UserDao {
    public void save(User user);
    public User findByUsername(String username);
    public User findByEmail(String email);
    public void delete(User user);
    public void update(User user);
    public User findById(int id);
}
