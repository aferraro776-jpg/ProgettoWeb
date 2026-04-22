package it.unical.progettoweb.dto.create;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BuildingLotCreateDTO extends RealEstateCreateDto {
    private Double cubature;
    private String plannedUse;
}
