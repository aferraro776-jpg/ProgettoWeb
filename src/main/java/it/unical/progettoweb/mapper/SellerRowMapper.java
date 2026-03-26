package it.unical.progettoweb.mapper;

import it.unical.progettoweb.model.Seller;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SellerRowMapper extends PersonRowMapper<Seller> {

    @Override
    public Seller mapRow(ResultSet rs,int numRow) throws SQLException {
        Seller seller = new Seller();
        mapPersonFields(seller, rs);
        seller.setVatNumber(rs.getString("vatnumber"));
        seller.setBirthDate(rs.getDate("birthdate"));
        return seller;
    }
}

