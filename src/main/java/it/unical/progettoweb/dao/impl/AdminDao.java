package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.PersonDao;
import it.unical.progettoweb.model.Admin;

import java.util.List;
import java.util.Optional;

public class AdminDao implements PersonDao<Admin> {
    @Override
    public Optional<Admin> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public void save(Admin entity) {

    }

    @Override
    public Optional<Admin> get(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Admin> getAll() {
        return List.of();
    }

    @Override
    public void update(Admin entity) {

    }

    @Override
    public void delete(Integer integer) {

    }
}
