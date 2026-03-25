package it.unical.progettoweb.model;

import lombok.Data;

@Data
public class Review {
    private int id;
    private String title;
    private String description;
    private int rating;
    private java.util.Date date;
    private int userId;
    private int realEstateId;
}
