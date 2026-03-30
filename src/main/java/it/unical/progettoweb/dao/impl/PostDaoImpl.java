package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.PostDao;
import it.unical.progettoweb.mapper.PostRowMapper;
import it.unical.progettoweb.model.Post;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostDaoImpl implements PostDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> rowMapper;

    public PostDaoImpl(JdbcTemplate jdbcTemplate, PostRowMapper postRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = postRowMapper;
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update(
                "INSERT INTO posts (id, title, description, \"previousPrice\", \"currentPrice\", \"createdAt\", \"idSeller\", \"idRealEstate\") " +
                        "VALUES (?, ?, ?, ?, ?, NOW(), ?, ?)",
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPreviousPrice(),
                post.getCurrentPrice(),
                post.getSellerId(),
                post.getRealEstateId()
        );
    }

    @Override
    public Optional<Post> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM posts WHERE id=?", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> getAll() {
        return jdbcTemplate.query("SELECT * FROM posts ORDER BY \"createdAt\" DESC", rowMapper);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title=?, description=?, \"previousPrice\"=?, \"currentPrice\"=?, \"idSeller\"=?, \"idRealEstate\"=? WHERE id=?",
                post.getTitle(),
                post.getDescription(),
                post.getPreviousPrice(),
                post.getCurrentPrice(),
                post.getSellerId(),
                post.getRealEstateId(),
                post.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id=?", id);
    }

    @Override
    public List<Post> findBySellerId(int sellerId) {
        return jdbcTemplate.query(
                "SELECT * FROM posts WHERE \"idSeller\"=? ORDER BY \"createdAt\" DESC",
                rowMapper, sellerId
        );
    }

    @Override
    public List<Post> findByRealEstateId(int realEstateId) {
        return jdbcTemplate.query(
                "SELECT * FROM posts WHERE \"idRealEstate\"=? ORDER BY \"createdAt\" DESC",
                rowMapper, realEstateId
        );
    }

    @Override
    public List<Post> findAllOrderByPriceAsc() {
        return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY \"currentPrice\" ASC",
                rowMapper
        );
    }

    @Override
    public List<Post> findAllOrderByPriceDesc() {
        return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY \"currentPrice\" DESC",
                rowMapper
        );
    }

    @Override
    public void reducePrice(int postId, double newPrice) {
        jdbcTemplate.update(
                "UPDATE posts SET \"previousPrice\"=\"currentPrice\", \"currentPrice\"=? WHERE id=?",
                newPrice, postId
        );
    }
}