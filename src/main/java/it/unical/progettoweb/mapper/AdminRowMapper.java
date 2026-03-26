package it.unical.progettoweb.mapper;

import it.unical.progettoweb.model.Admin;
import org.springframework.jdbc.core.RowMapper;

public class AdminRowMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setName(rs.getString("name"));
        admin.setSurname(rs.getString("surname"));
        admin.setPassword(rs.getString("password"));
        admin.setEmail(rs.getString("email"));
        return admin;
    }
}
