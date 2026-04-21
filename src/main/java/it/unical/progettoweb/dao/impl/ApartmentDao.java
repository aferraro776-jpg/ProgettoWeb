package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.RealEstateDao;
import it.unical.progettoweb.mapper.RealEstateRowMapper;
import it.unical.progettoweb.model.Apartment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApartmentDao implements RealEstateDao<Apartment> {

    private final JdbcTemplate jdbcTemplate;
    private final RealEstateRowMapper realEstateRowMapper;
    private final RowMapper<Apartment> rowMapper;

    public ApartmentDao(JdbcTemplate jdbcTemplate, RealEstateRowMapper realEstateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.realEstateRowMapper = realEstateRowMapper;
        this.rowMapper = (rs, rowNum) -> {
            Apartment a = new Apartment();
            realEstateRowMapper.mapCommon(a, rs);
            a.setNumberOfRooms(rs.getInt("numberOfRooms"));
            a.setFloor(rs.getInt("floor"));
            a.setHasElevator(rs.getBoolean("hasElevator"));
            return a;
        };
    }

    @Override
    public Apartment save(Apartment a) {
        jdbcTemplate.update(
                "INSERT INTO \"realEstate\" (id, title, description, \"squareMetres\", latit, longit, address, \"createdAt\", type, \"numberOfRooms\", floor, \"hasElevator\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'APARTMENT', ?, ?, ?)",
                a.getId(), a.getTitle(), a.getDescription(), a.getSquareMetres(),
                a.getLatit(), a.getLongit(), a.getAddress(),
                a.getNumberOfRooms(), a.getFloor(), a.getHasElevator()
        );
        return a;
    }

    @Override
    public Optional<Apartment> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM \"realEstate\" WHERE id=? AND type='APARTMENT'", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Apartment> getAll() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='APARTMENT' ORDER BY id", rowMapper);
    }

    @Override
    public Apartment update(Apartment a) {
        jdbcTemplate.update(
                "UPDATE \"realEstate\" SET title=?, description=?, \"squareMetres\"=?, latit=?, longit=?, address=?, \"numberOfRooms\"=?, floor=?, \"hasElevator\"=? WHERE id=?",
                a.getTitle(), a.getDescription(), a.getSquareMetres(),
                a.getLatit(), a.getLongit(), a.getAddress(),
                a.getNumberOfRooms(), a.getFloor(), a.getHasElevator(), a.getId()
        );
        return a;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM \"realEstate\" WHERE id=?", id);
    }

    @Override
    public List<Apartment> findAllOrderBySquareMetresAsc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='APARTMENT' ORDER BY \"squareMetres\" ASC", rowMapper);
    }

    @Override
    public List<Apartment> findAllOrderBySquareMetresDesc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='APARTMENT' ORDER BY \"squareMetres\" DESC", rowMapper);
    }

    @Override
    public List<Apartment> findAllOrderByPriceAsc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='APARTMENT' ORDER BY p.\"currentPrice\" ASC",
                rowMapper
        );
    }

    @Override
    public List<Apartment> findAllOrderByPriceDesc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='APARTMENT' ORDER BY p.\"currentPrice\" DESC",
                rowMapper
        );
    }
}