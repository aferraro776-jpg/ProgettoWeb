package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.PersonDao;
import it.unical.progettoweb.mapper.SellerRowMapper;
import it.unical.progettoweb.model.Seller;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class SellerDao implements PersonDao<Seller> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Seller> rowMapper;

    public SellerDao(JdbcTemplate jdbc, SellerRowMapper mapper) {
        this.jdbcTemplate = jdbc;
        this.rowMapper = mapper;
    }

    @Override
    public void save(Seller seller) {
        jdbcTemplate.update(
                "INSERT INTO sellers (id, vatnumber, name, surname, email, birthdate, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                seller.getId(),
                seller.getVatNumber(),
                seller.getName(),
                seller.getSurname(),
                seller.getEmail(),
                seller.getBirthDate(),
                seller.getPassword()
        );
    }

    @Override
    public Optional<Seller> get(Integer id) {
        try {
            Seller seller = jdbcTemplate.queryForObject(
                    "SELECT * FROM sellers WHERE id = ?",
                    rowMapper, id
            );
            return Optional.ofNullable(seller);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Seller> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM sellers ORDER BY id",
                rowMapper
        );
    }

    @Override
    public void update(Seller seller) {
        jdbcTemplate.update(
                "UPDATE sellers SET vatnumber=?, name=?, surname=?, email=?, birthdate=?, password=? WHERE id=?",
                seller.getVatNumber(),
                seller.getName(),
                seller.getSurname(),
                seller.getEmail(),
                seller.getBirthDate(),
                seller.getPassword(),
                seller.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM sellers WHERE id = ?", id);
    }



    public Optional<Seller> findByVatNumber(String vatNumber) {
        try {
            Seller seller = jdbcTemplate.queryForObject(
                    "SELECT * FROM sellers WHERE vatnumber = ?",
                    rowMapper, vatNumber
            );
            return Optional.ofNullable(seller);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByVatNumber(String vatNumber) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sellers WHERE vatnumber = ?",
                Integer.class, vatNumber
        );
        return count != null && count > 0;
    }

    @Override
    public Optional<Seller> findByEmail(String email) {
        try {
            Seller seller = jdbcTemplate.queryForObject(
                    "SELECT * FROM sellers WHERE email = ?",
                    rowMapper, email
            );
            return Optional.ofNullable(seller);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sellers WHERE email = ?",
                Integer.class, email
        );
        return count != null && count > 0;
    }
}
