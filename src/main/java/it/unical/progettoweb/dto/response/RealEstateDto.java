package it.unical.progettoweb.dto.response;

import java.time.LocalDateTime;

public class RealEstateDto {

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

    public RealEstateDto() {}

    public RealEstateDto(int id, String title, int numberOfRooms, String description,
                         double squareMetres, double latit, double longit,
                         String address, LocalDateTime createdAt, String type) {
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
}