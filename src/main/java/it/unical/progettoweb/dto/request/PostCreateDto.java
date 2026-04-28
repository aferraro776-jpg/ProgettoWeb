package it.unical.progettoweb.dto.request;

import it.unical.progettoweb.model.Photo;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateDto {

    private String title;
    private String description;
    private double currentPrice;
    private int sellerId;
    private int realEstateId;
    private List<Photo> photoUrls;
}