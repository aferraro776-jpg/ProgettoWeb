package it.unical.progettoweb.mapper;

import it.unical.progettoweb.model.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setDescription(rs.getString("description"));
        post.setPreviousPrice(rs.getDouble("previousPrice"));
        post.setCurrentPrice(rs.getDouble("currentPrice"));
        post.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        post.setSellerId(rs.getInt("idSeller"));
        post.setRealEstateId(rs.getInt("idRealEstate"));
        return post;
    }
}