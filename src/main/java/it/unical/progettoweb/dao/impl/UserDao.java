package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.PersonDao;
import it.unical.progettoweb.mapper.UserRowMapper;
import it.unical.progettoweb.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements PersonDao<User> {

    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    public UserDao(JdbcTemplate jdbc, UserRowMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public void save(User user) {
        jdbc.update(
                "INSERT INTO users (id, name, surname, password, email, birthDate) VALUES (?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAuthProvider(),

                "INSERT INTO users (id, name, surname, password, email, birthDate, auth_provider, \"isBanned\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getSurname(), user.getPassword(),
                user.getEmail(), user.getBirthDate(), user.getAuthProvider(), user.isBanned()
        );
    }

    @Override
    public Optional<User> get(Integer id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("SELECT * FROM users WHERE id = ?", mapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM users", mapper);
    }

    @Override
    public void update(User user) {
        jdbc.update(
                "UPDATE users SET name=?, surname=?, password=?, email=?, birthDate=? WHERE id=?",
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getEmail(),
                user.getBirthDate(),
                user.getId(),

                "UPDATE users SET name=?, surname=?, password=?, email=?, birthDate=?, \"isBanned\"=? WHERE id=?",
                user.getName(), user.getSurname(), user.getPassword(),
                user.getEmail(), user.getBirthDate(), user.isBanned(), user.getId()
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
    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }
}
