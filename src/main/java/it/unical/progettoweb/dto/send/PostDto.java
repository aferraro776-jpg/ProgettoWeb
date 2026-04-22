package it.unical.progettoweb.dto.send;

import java.time.LocalDateTime;

public class PostDto {

    private int id;
    private String title;
    private String description;
    private double previousPrice;
    private double currentPrice;
    private LocalDateTime createdAt;
    private int sellerId;
    private int realEstateId;

    public PostDto() {}

    public PostDto(int id, String title, String description, double previousPrice,
                   double currentPrice, LocalDateTime createdAt,
                   int sellerId, int realEstateId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
        this.createdAt = createdAt;
        this.sellerId = sellerId;
        this.realEstateId = realEstateId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPreviousPrice() { return previousPrice; }
    public void setPreviousPrice(double previousPrice) { this.previousPrice = previousPrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public int getRealEstateId() { return realEstateId; }
    public void setRealEstateId(int realEstateId) { this.realEstateId = realEstateId; }
}