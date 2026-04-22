package it.unical.progettoweb.dto.send;

import java.util.Date;

public class ReviewDto {

    private int id;
    private String title;
    private String description;
    private int rating;
    private Date date;
    private int userId;
    private int realEstateId;

    public ReviewDto() {}

    public ReviewDto(int id, String title, String description, int rating,
                     Date date, int userId, int realEstateId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.date = date;
        this.userId = userId;
        this.realEstateId = realEstateId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRealEstateId() { return realEstateId; }
    public void setRealEstateId(int realEstateId) { this.realEstateId = realEstateId; }
}