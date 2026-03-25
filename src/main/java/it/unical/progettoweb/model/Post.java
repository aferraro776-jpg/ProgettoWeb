package it.unical.progettoweb.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private int id;
    private String title;
    private String description;
    private double previousPrice;
    private double currentPrice;
    private LocalDateTime createdAt;
    private int sellerId;
    private int realEstateId;

}
