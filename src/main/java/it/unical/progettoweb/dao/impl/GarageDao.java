package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.RealEstateDao;
import it.unical.progettoweb.mapper.RealEstateRowMapper;
import it.unical.progettoweb.model.Garage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GarageDao implements RealEstateDao<Garage> {

    private final JdbcTemplate jdbcTemplate;
    private final RealEstateRowMapper realEstateRowMapper;
    private final RowMapper<Garage> rowMapper;

    public GarageDao(JdbcTemplate jdbcTemplate, RealEstateRowMapper realEstateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.realEstateRowMapper = realEstateRowMapper;
        this.rowMapper = (rs, rowNum) -> {
            Garage g = new Garage();
            realEstateRowMapper.mapCommon(g, rs);
            g.setWidth(rs.getDouble("width"));
            g.setHeight(rs.getDouble("height"));
            g.setIsElectric(rs.getBoolean("isElectric"));
            return g;
        };
    }

    @Override
    public void save(Garage g) {
        jdbcTemplate.update(
                "INSERT INTO \"realEstate\" (id, title, description, \"squareMetres\", latit, longit, address, \"createdAt\", type, width, height, \"isElectric\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'GARAGE', ?, ?, ?)",
                g.getId(), g.getTitle(), g.getDescription(), g.getSquareMetres(),
                g.getLatit(), g.getLongit(), g.getAddress(),
                g.getWidth(), g.getHeight(), g.getIsElectric()
        );
    }

    @Override
    public Optional<Garage> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM \"realEstate\" WHERE id=? AND type='GARAGE'", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Garage> getAll() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='GARAGE' ORDER BY id", rowMapper);
    }

    @Override
    public void update(Garage g) {
        jdbcTemplate.update(
                "UPDATE \"realEstate\" SET title=?, description=?, \"squareMetres\"=?, latit=?, longit=?, address=?, width=?, height=?, \"isElectric\"=? WHERE id=?",
                g.getTitle(), g.getDescription(), g.getSquareMetres(),
                g.getLatit(), g.getLongit(), g.getAddress(),
                g.getWidth(), g.getHeight(), g.getIsElectric(), g.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM \"realEstate\" WHERE id=?", id);
    }

    @Override
    public List<Garage> findAllOrderBySquareMetresAsc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='GARAGE' ORDER BY \"squareMetres\" ASC", rowMapper);
    }

    @Override
    public List<Garage> findAllOrderBySquareMetresDesc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='GARAGE' ORDER BY \"squareMetres\" DESC", rowMapper);
    }

    @Override
    public List<Garage> findAllOrderByPriceAsc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='GARAGE' ORDER BY p.\"currentPrice\" ASC",
                rowMapper
        );
    }

    @Override
    public List<Garage> findAllOrderByPriceDesc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='GARAGE' ORDER BY p.\"currentPrice\" DESC",
                rowMapper
        );
    }
}