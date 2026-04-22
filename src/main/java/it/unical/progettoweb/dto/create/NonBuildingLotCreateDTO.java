package it.unical.progettoweb.dto.create;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NonBuildingLotCreateDTO extends RealEstateCreateDto {
    private String cropType;
}
