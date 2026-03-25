package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.UserDao;
import it.unical.progettoweb.mapper.UserRowMapper;
import it.unical.progettoweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    public UserDaoImpl(JdbcTemplate jdbc, UserRowMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public void save(User user, Integer id) {
        jdbc.update(
                "INSERT INTO users (id, username, password, email, birth_date) VALUES (?, ?, ?, ?, ?)",
                id, user.getUsername(), user.getPassword(), user.getEmail(), user.getBirthDate()
        );
    }

    @Override
    public Optional<User> get(Integer id) {
        try {
            User user = jdbc.queryForObject("SELECT * FROM users WHERE id = ?", mapper, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM users", mapper);
    }

    @Override
    public void update(User user, Integer id) {
        jdbc.update(
                "UPDATE users SET username=?, password=?, email=?, birth_date=? WHERE id=?",
                user.getUsername(), user.getPassword(), user.getEmail(), user.getBirthDate(), id
        );
    }

    @Override
    public void delete(Integer id) {
        jdbc.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(jdbc.queryForObject("SELECT * FROM users WHERE email = ?", mapper, email));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUtenti() {
        return getAll();
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return count > 0;
    }
}
