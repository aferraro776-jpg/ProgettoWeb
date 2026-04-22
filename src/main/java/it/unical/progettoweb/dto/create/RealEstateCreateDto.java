package it.unical.progettoweb.dto.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApartmentCreateDTO.class,       name = "APARTMENT"),
        @JsonSubTypes.Type(value = VillaCreateDTO.class,           name = "VILLA"),
        @JsonSubTypes.Type(value = GarageCreateDTO.class,          name = "GARAGE"),
        @JsonSubTypes.Type(value = BuildingLotCreateDTO.class,     name = "BUILDING_LOT"),
        @JsonSubTypes.Type(value = NonBuildingLotCreateDTO.class,  name = "NON_BUILDING_LOT")
})
@Data
public abstract class RealEstateCreateDto {
    private String type;
    private String title;
    private int numberOfRooms;
    private String description;
    private double squareMetres;
    private double latit;
    private double longit;
    private String address;
}
