package it.unical.progettoweb.dto.create;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GarageCreateDTO extends RealEstateCreateDto {
    private Double width;
    private Double height;
    private Boolean isElectric;
}
