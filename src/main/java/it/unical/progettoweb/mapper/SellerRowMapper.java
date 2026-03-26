package it.unical.progettoweb.mapper;

import it.unical.progettoweb.model.Seller;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SellerRowMapper implements RowMapper<Seller> {

    @Override
    public Seller mapRow(ResultSet rs, int rowNum) throws SQLException {
        Seller seller = new Seller();

        seller.setId(rs.getInt("id"));
        seller.setEmail(rs.getString("email"));
        seller.setVatNumber(rs.getString("vatNumber"));
        seller.setPassword(rs.getString("password"));
        seller.setName(rs.getString("name"));
        seller.setSurname(rs.getString("surname"));
        seller.setBirthDate(rs.getDate("birthDate"));

        return seller;
    }
}
