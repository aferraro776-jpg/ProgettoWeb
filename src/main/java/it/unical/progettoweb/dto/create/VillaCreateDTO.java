package it.unical.progettoweb.dto.create;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VillaCreateDTO extends RealEstateCreateDto {
    private Boolean hasGarden;
    private Boolean hasPool;
    private Integer numberOfFloors;
}
