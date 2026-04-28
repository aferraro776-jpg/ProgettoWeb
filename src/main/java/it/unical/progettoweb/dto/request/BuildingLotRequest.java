package it.unical.progettoweb.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BuildingLotRequest extends RealEstateCreateRequest {
    private Double cubature;
    private String plannedUse;
}
