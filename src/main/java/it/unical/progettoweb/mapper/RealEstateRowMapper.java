package it.unical.progettoweb.mapper;

import it.unical.progettoweb.model.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RealEstateRowMapper implements RowMapper<RealEstate> {

    @Override
    public RealEstate mapRow(ResultSet rs, int rowNum) throws SQLException {
        String type = rs.getString("type");

        RealEstate realEstate = switch (type) {
            case "APARTMENT"           -> mapApartment(rs);
            case "VILLA"               -> mapVilla(rs);
            case "GARAGE"              -> mapGarage(rs);
            case "BUILDING_LOT"        -> mapBuildingLot(rs);
            case "NON_BUILDING_LOT"    -> mapNonBuildingLot(rs);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };

        mapCommon(realEstate, rs);
        return realEstate;
    }

    public void mapCommon(RealEstate r, ResultSet rs) throws SQLException {
        r.setId(rs.getInt("id"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));
        r.setSquareMetres(rs.getDouble("squareMetres"));
        r.setLatit(rs.getDouble("latit"));
        r.setLongit(rs.getDouble("longit"));
        r.setAddress(rs.getString("address"));
        r.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        r.setType(rs.getString("type"));
    }

    private Apartment mapApartment(ResultSet rs) throws SQLException {
        Apartment a = new Apartment();
        a.setNumberOfRooms(rs.getInt("numberOfRooms"));
        a.setFloor(rs.getInt("floor"));
        a.setHasElevator(rs.getBoolean("hasElevator"));
        return a;
    }

    private Villa mapVilla(ResultSet rs) throws SQLException {
        Villa v = new Villa();
        v.setNumberOfRooms(rs.getInt("numberOfRooms"));
        v.setHasGarden(rs.getBoolean("hasGarden"));
        v.setHasPool(rs.getBoolean("hasPool"));
        v.setNumberOfFloors(rs.getInt("numberOfFloors"));
        return v;
    }

    private Garage mapGarage(ResultSet rs) throws SQLException {
        Garage g = new Garage();
        g.setWidth(rs.getDouble("width"));
        g.setHeight(rs.getDouble("height"));
        g.setIsElectric(rs.getBoolean("isElectric"));
        return g;
    }

    private BuildingLot mapBuildingLot(ResultSet rs) throws SQLException {
        BuildingLot b = new BuildingLot();
        b.setCubature(rs.getDouble("cubature"));
        b.setPlannedUse(rs.getString("landUse"));
        return b;
    }

    private NonBuildingLot mapNonBuildingLot(ResultSet rs) throws SQLException {
        NonBuildingLot n = new NonBuildingLot();
        n.setCropType(rs.getString("cropType"));
        return n;
    }
}