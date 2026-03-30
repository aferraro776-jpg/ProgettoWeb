package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.ReviewDao;
import it.unical.progettoweb.mapper.ReviewRowMapper;
import it.unical.progettoweb.model.Review;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Review> rowMapper;

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate, ReviewRowMapper reviewRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = reviewRowMapper;
    }

    @Override
    public void save(Review review) {
        jdbcTemplate.update(
                "INSERT INTO reviews (id, title, description, rating, date, \"idUser\", \"idRealEstate\") " +
                        "VALUES (?, ?, ?, ?, CURRENT_DATE, ?, ?)",
                review.getId(),
                review.getTitle(),
                review.getDescription(),
                review.getRating(),
                review.getUserId(),
                review.getRealEstateId()
        );
    }

    @Override
    public Optional<Review> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM reviews WHERE id = ?", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query("SELECT * FROM reviews ORDER BY date DESC", rowMapper);
    }

    @Override
    public void update(Review review) {
        jdbcTemplate.update(
                "UPDATE reviews SET title=?, description=?, rating=?, \"idUser\"=?, \"idRealEstate\"=? WHERE id=?",
                review.getTitle(),
                review.getDescription(),
                review.getRating(),
                review.getUserId(),
                review.getRealEstateId(),
                review.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM reviews WHERE id = ?", id);
    }

    @Override
    public List<Review> findByRealEstateId(int realEstateId) {
        return jdbcTemplate.query(
                "SELECT * FROM reviews WHERE \"idRealEstate\" = ? ORDER BY date DESC",
                rowMapper, realEstateId
        );
    }

    @Override
    public List<Review> findByUserId(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM reviews WHERE \"idUser\" = ? ORDER BY date DESC",
                rowMapper, userId
        );
    }

    @Override
    public Double getAverageRatingForRealEstate(int realEstateId) {
        Double avg = jdbcTemplate.queryForObject(
                "SELECT AVG(rating) FROM reviews WHERE \"idRealEstate\" = ?",
                Double.class, realEstateId
        );
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
}