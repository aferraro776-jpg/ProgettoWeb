package it.unical.progettoweb.controller;

import it.unical.progettoweb.dto.request.RealEstateRequest;
import it.unical.progettoweb.dto.response.RealEstateDto;
import it.unical.progettoweb.service.RealEstateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realestate")
public class RealEstateController {

    private final RealEstateService realEstateService;

    public RealEstateController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @GetMapping
    public ResponseEntity<List<RealEstateDto>> getAll(){
        return ResponseEntity.ok(realEstateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        try {
            return ResponseEntity.ok(realEstateService.findById(id));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<RealEstateDto> create(@RequestBody RealEstateRequest realEstate){
        RealEstateDto savedRealEstate = realEstateService.save(realEstate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRealEstate);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody RealEstateRequest realEstate){
        try {
            RealEstateDto updated = realEstateService.update(id, realEstate);
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        try{
            realEstateService.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
