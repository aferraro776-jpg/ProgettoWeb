package it.unical.progettoweb.dao.impl;

import it.unical.progettoweb.dao.PhotoDao;
import it.unical.progettoweb.mapper.PhotoRowMapper;
import it.unical.progettoweb.model.Photo;
import it.unical.progettoweb.proxy.PhotoCollection;
import it.unical.progettoweb.proxy.PhotoProxy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PhotoDaoImpl implements PhotoDao {

    private final JdbcTemplate jdbcTemplate;
    private final PhotoRowMapper photoRowMapper;
    private final RowMapper<Photo> rowMapper;

    public PhotoDaoImpl(JdbcTemplate jdbcTemplate, PhotoRowMapper photoRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.photoRowMapper = photoRowMapper;
        this.rowMapper = photoRowMapper;
    }

    @Override
    public void save(Photo photo) {
        jdbcTemplate.update(
                "INSERT INTO photos (id, url, \"postId\") VALUES (?, ?, ?)",
                photo.getId(),
                photo.getUrl(),
                photo.getPostId()
        );
    }

    @Override
    public Optional<Photo> get(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM photos WHERE id = ?", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Photo> getAll() {
        return jdbcTemplate.query("SELECT * FROM photos ORDER BY id", rowMapper);
    }

    @Override
    public void update(Photo photo) {
        jdbcTemplate.update(
                "UPDATE photos SET url=?, \"postId\"=? WHERE id=?",
                photo.getUrl(),
                photo.getPostId(),
                photo.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM photos WHERE id = ?", id);
    }

    @Override
    public List<Photo> findByPostId(int postId) {
        return jdbcTemplate.query(
                "SELECT * FROM photos WHERE \"postId\" = ? ORDER BY id",
                rowMapper, postId
        );
    }

    @Override
    public PhotoCollection getPhotoCollectionForPost(int postId) {
        return new PhotoProxy(postId, jdbcTemplate, photoRowMapper);
    }
}