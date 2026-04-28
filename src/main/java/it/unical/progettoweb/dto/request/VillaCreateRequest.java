package it.unical.progettoweb.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VillaCreateRequest extends RealEstateCreateRequest {
    private Boolean hasGarden;
    private Boolean hasPool;
    private Integer numberOfFloors;
}
