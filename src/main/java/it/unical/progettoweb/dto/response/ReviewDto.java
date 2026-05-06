package it.unical.progettoweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int id;
    private String title;
    private String description;
    private int rating;
    private Date date;
    private int userId;
    private int realEstateId;
}
