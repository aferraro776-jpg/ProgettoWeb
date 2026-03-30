package it.unical.progettoweb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Villa extends RealEstate {
    private Integer numberOfRooms;
    private Boolean hasGarden;
    private Boolean hasPool;
    private Integer numberOfFloors;
}