package it.unical.progettoweb.dto.create;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApartmentCreateDTO extends RealEstateCreateDto {
    private Integer floor;
    private Boolean hasElevator;
}
