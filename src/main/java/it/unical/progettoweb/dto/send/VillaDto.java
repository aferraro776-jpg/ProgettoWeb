package it.unical.progettoweb.dto.send;

import java.time.LocalDateTime;

// Classe piatta: contiene sia i campi di RealEstate che quelli specifici di Villa
public class VillaDto {

    // Campi ereditati da RealEstate
    private int id;
    private String title;
    private int numberOfRooms;
    private String description;
    private double squareMetres;
    private double latit;
    private double longit;
    private String address;
    private LocalDateTime createdAt;
    private String type;

    // Campi specifici di Villa
    private Boolean hasGarden;
    private Boolean hasPool;
    private Integer numberOfFloors;

    public VillaDto() {}

    public VillaDto(int id, String title, int numberOfRooms, String description,
                    double squareMetres, double latit, double longit,
                    String address, LocalDateTime createdAt, String type,
                    Boolean hasGarden, Boolean hasPool, Integer numberOfFloors) {
        this.id = id;
        this.title = title;
        this.numberOfRooms = numberOfRooms;
        this.description = description;
        this.squareMetres = squareMetres;
        this.latit = latit;
        this.longit = longit;
        this.address = address;
        this.createdAt = createdAt;
        this.type = type;
        this.hasGarden = hasGarden;
        this.hasPool = hasPool;
        this.numberOfFloors = numberOfFloors;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getNumberOfRooms() { return numberOfRooms; }
    public void setNumberOfRooms(int numberOfRooms) { this.numberOfRooms = numberOfRooms; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getSquareMetres() { return squareMetres; }
    public void setSquareMetres(double squareMetres) { this.squareMetres = squareMetres; }

    public double getLatit() { return latit; }
    public void setLatit(double latit) { this.latit = latit; }

    public double getLongit() { return longit; }
    public void setLongit(double longit) { this.longit = longit; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getHasGarden() { return hasGarden; }
    public void setHasGarden(Boolean hasGarden) { this.hasGarden = hasGarden; }

    public Boolean getHasPool() { return hasPool; }
    public void setHasPool(Boolean hasPool) { this.hasPool = hasPool; }

    public Integer getNumberOfFloors() { return numberOfFloors; }
    public void setNumberOfFloors(Integer numberOfFloors) { this.numberOfFloors = numberOfFloors; }
}