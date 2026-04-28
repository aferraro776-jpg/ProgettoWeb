package it.unical.progettoweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private int id;
    private String title;
    private String description;
    private double previousPrice;
    private double currentPrice;
    private LocalDateTime createdAt;
    private int sellerId;
    private int realEstateId;
    private List<String> photoUrls;
}