package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.RealEstateDao;
import it.unical.progettoweb.mapper.RealEstateRowMapper;
import it.unical.progettoweb.model.NonBuildingLot;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NonBuildingLotDao implements RealEstateDao<NonBuildingLot> {

    private final JdbcTemplate jdbcTemplate;
    private final RealEstateRowMapper realEstateRowMapper;
    private final RowMapper<NonBuildingLot> rowMapper;

    public NonBuildingLotDao(JdbcTemplate jdbcTemplate, RealEstateRowMapper realEstateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.realEstateRowMapper = realEstateRowMapper;
        this.rowMapper = (rs, rowNum) -> {
            NonBuildingLot n = new NonBuildingLot();
            realEstateRowMapper.mapCommon(n, rs);
            n.setCropType(rs.getString("cropType"));
            return n;
        };
    }

    @Override
    public void save(NonBuildingLot n) {
        jdbcTemplate.update(
                "INSERT INTO \"realEstate\" (id, title, description, \"squareMetres\", latit, longit, address, \"createdAt\", type, \"cropType\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'NON_BUILDING_LOT', ?)",
                n.getId(), n.getTitle(), n.getDescription(), n.getSquareMetres(),
                n.getLatit(), n.getLongit(), n.getAddress(),
                n.getCropType()
        );
    }

    @Override
    public Optional<NonBuildingLot> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM \"realEstate\" WHERE id=? AND type='NON_BUILDING_LOT'", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<NonBuildingLot> getAll() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='NON_BUILDING_LOT' ORDER BY id", rowMapper);
    }

    @Override
    public void update(NonBuildingLot n) {
        jdbcTemplate.update(
                "UPDATE \"realEstate\" SET title=?, description=?, \"squareMetres\"=?, latit=?, longit=?, address=?, \"cropType\"=? WHERE id=?",
                n.getTitle(), n.getDescription(), n.getSquareMetres(),
                n.getLatit(), n.getLongit(), n.getAddress(),
                n.getCropType(), n.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM \"realEstate\" WHERE id=?", id);
    }

    @Override
    public List<NonBuildingLot> findAllOrderBySquareMetresAsc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='NON_BUILDING_LOT' ORDER BY \"squareMetres\" ASC", rowMapper);
    }

    @Override
    public List<NonBuildingLot> findAllOrderBySquareMetresDesc() {
        return jdbcTemplate.query("SELECT * FROM \"realEstate\" WHERE type='NON_BUILDING_LOT' ORDER BY \"squareMetres\" DESC", rowMapper);
    }

    @Override
    public List<NonBuildingLot> findAllOrderByPriceAsc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='NON_BUILDING_LOT' ORDER BY p.\"currentPrice\" ASC",
                rowMapper
        );
    }

    @Override
    public List<NonBuildingLot> findAllOrderByPriceDesc() {
        return jdbcTemplate.query(
                "SELECT r.* FROM \"realEstate\" r JOIN posts p ON p.\"idRealEstate\"=r.id WHERE r.type='NON_BUILDING_LOT' ORDER BY p.\"currentPrice\" DESC",
                rowMapper
        );
    }
}