package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.*;

import it.unical.progettoweb.dto.request.*;
import it.unical.progettoweb.dto.response.RealEstateDto;
import it.unical.progettoweb.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RealEstateService {

    private final ApartmentDao apartmentDao;
    private final VillaDao villaDao;
    private final GarageDao garageDao;
    private final BuildingLotDao buildingLotDao;
    private final NonBuildingLotDao nonBuildingLotDao;
    private final RealEstateDaoImpl realEstateDao;
    private final Random random = new Random();


    public RealEstateDto save(RealEstateCreateRequest dto) {
        return switch (dto) {
            case ApartmentRequest d -> {
                Apartment e = new Apartment();
                mapCommon(e, d);
                e.setFloor(d.getFloor());
                e.setHasElevator(d.getHasElevator());
                yield toDto(apartmentDao.save(e));
            }
            case VillaCreateRequest d -> {
                Villa e = new Villa();
                mapCommon(e, d);
                e.setHasGarden(d.getHasGarden());
                e.setHasPool(d.getHasPool());
                e.setNumberOfFloors(d.getNumberOfFloors());
                yield toDto(villaDao.save(e));
            }
            case GarageRequest d -> {
                Garage e = new Garage();
                mapCommon(e, d);
                e.setWidth(d.getWidth());
                e.setHeight(d.getHeight());
                e.setIsElectric(d.getIsElectric());
                yield toDto(garageDao.save(e));
            }
            case BuildingLotRequest d -> {
                BuildingLot e = new BuildingLot();
                mapCommon(e, d);
                e.setCubature(d.getCubature());
                e.setPlannedUse(d.getPlannedUse());
                yield toDto(buildingLotDao.save(e));
            }
            case NonBuildingLotRequest d -> {
                NonBuildingLot e = new NonBuildingLot();
                mapCommon(e, d);
                e.setCropType(d.getCropType());
                yield toDto(nonBuildingLotDao.save(e));
            }
            default -> throw new IllegalArgumentException("Tipo non supportato");
        };
    }


    public List<RealEstateDto> findAll() {
        List<RealEstate> realEstates = realEstateDao.findAll();
        List<RealEstateDto> results = new ArrayList<>();
        for(RealEstate e : realEstates) {
            results.add(toDto(e));
        }
        return results;
    }

    public RealEstateDto update(int id, RealEstateCreateRequest dto) {
        return switch (dto) {
            case ApartmentRequest d -> {
                Apartment e = new Apartment();
                mapCommon(e, d);
                e.setId(id);
                e.setFloor(d.getFloor());
                e.setHasElevator(d.getHasElevator());
                yield toDto(apartmentDao.update(e));
            }
            case VillaCreateRequest d -> {
                Villa e = new Villa();
                mapCommon(e, d);
                e.setId(id);
                e.setHasGarden(d.getHasGarden());
                e.setHasPool(d.getHasPool());
                e.setNumberOfFloors(d.getNumberOfFloors());
                yield toDto(villaDao.update(e));
            }
            case GarageRequest d -> {
                Garage e = new Garage();
                mapCommon(e, d);
                e.setId(id);
                e.setWidth(d.getWidth());
                e.setHeight(d.getHeight());
                e.setIsElectric(d.getIsElectric());
                yield toDto(garageDao.update(e));
            }
            case BuildingLotRequest d -> {
                BuildingLot e = new BuildingLot();
                mapCommon(e, d);
                e.setId(id);
                e.setCubature(d.getCubature());
                e.setPlannedUse(d.getPlannedUse());
                yield toDto(buildingLotDao.update(e));
            }
            case NonBuildingLotRequest d -> {
                NonBuildingLot e = new NonBuildingLot();
                mapCommon(e, d);
                e.setId(id);
                e.setCropType(d.getCropType());
                yield toDto(nonBuildingLotDao.update(e));
            }
            default -> throw new IllegalArgumentException("Tipo non supportato");
        };
    }

    public void delete(int id)  {
        Optional<RealEstate> realEstate = realEstateDao.findById(id);
        if (realEstate.isEmpty()) {
            throw new RuntimeException("RealEstate non trovato");
        }
        realEstateDao.delete(id);
    }

    public RealEstateDto findById(int id) {
        Optional<RealEstate> realEstate = realEstateDao.findById(id);

        if (realEstate.isEmpty()) {
            throw new RuntimeException("RealEstate non trovato");
        }

        return toDto(realEstate.get());
    }

    private void mapCommon(RealEstate entity, RealEstateCreateRequest dto) {
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setNumberOfRooms(dto.getNumberOfRooms());
        entity.setDescription(dto.getDescription());
        entity.setSquareMetres(dto.getSquareMetres());
        entity.setLatit(dto.getLatit());
        entity.setLongit(dto.getLongit());
        entity.setAddress(dto.getAddress());
        entity.setId(generateUserId());
    }

    private RealEstateDto toDto(RealEstate e) {
        return new RealEstateDto(e.getId(), e.getTitle(), e.getNumberOfRooms(), e.getDescription(),
                e.getSquareMetres(), e.getLatit(), e.getLongit(), e.getAddress(), e.getCreatedAt(), e.getType());
    }
    private int generateUserId() {
        int id;
        do {
            id = random.nextInt(89999) + 10000;
        } while (realEstateDao.findById(id).isPresent());
        return id;
    }
}