package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.RealEstateDao;
import it.unical.progettoweb.mapper.RealEstateRowMapper;
import it.unical.progettoweb.model.BuildingLot;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BuildingLotDao implements RealEstateDao<BuildingLot> {

    private final JdbcTemplate jdbcTemplate;
    private final RealEstateRowMapper realEstateRowMapper;
    private final RowMapper<BuildingLot> rowMapper;

    public BuildingLotDao(JdbcTemplate jdbcTemplate, RealEstateRowMapper realEstateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.realEstateRowMapper = realEstateRowMapper;
        this.rowMapper = (rs, rowNum) -> {
            BuildingLot b = new BuildingLot();
            realEstateRowMapper.mapCommon(b, rs);
            b.setCubature(rs.getDouble("cubature"));
            b.setPlannedUse(rs.getString("landUse"));
            return b;
        };
    }

    @Override
    public BuildingLot save(BuildingLot b) {
        jdbcTemplate.update(
                "INSERT INTO \"realEstate\" (id, title, description, \"squareMetres\", latit, longit, address, \"createdAt\", type, cubature, \"landUse\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'BUILDING_LOT', ?, ?)",
                b.getId(), b.getTitle(), b.getDescription(), b.getSquareMetres(),
                b.getLatit(), b.getLongit(), b.getAddress(),
                b.getCubature(), b.getPlannedUse()
        );
        return b;
    }

    @Override
    public Optional<BuildingLot> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM \"realEstate\" WHERE id=? AND type='BUILDING_LOT'", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BuildingLot> getAll() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='BUILDING_LOT' ORDER BY id", rowMapper);
    }

    @Override
    public void update(BuildingLot b) {
        jdbcTemplate.update(
                "UPDATE \"realEstate\" SET title=?, description=?, \"squareMetres\"=?, latit=?, longit=?, address=?, cubature=?, \"landUse\"=? WHERE id=?",
                b.getTitle(), b.getDescription(), b.getSquareMetres(),
                b.getLatit(), b.getLongit(), b.getAddress(),
                b.getCubature(), b.getPlannedUse(), b.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM \"realEstate\" WHERE id=?", id);
    }

    @Override
    public List<BuildingLot> findAllOrderBySquareMetresAsc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='BUILDING_LOT' ORDER BY \"squareMetres\" ASC", rowMapper);
    }

    @Override
    public List<BuildingLot> findAllOrderBySquareMetresDesc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='BUILDING_LOT' ORDER BY \"squareMetres\" DESC", rowMapper);
    }

    @Override
    public List<BuildingLot> findAllOrderByPriceAsc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='BUILDING_LOT' ORDER BY p.\"currentPrice\" ASC",
                rowMapper
        );
    }

    @Override
    public List<BuildingLot> findAllOrderByPriceDesc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='BUILDING_LOT' ORDER BY p.\"currentPrice\" DESC",
                rowMapper
        );
    }
}