package it.unical.progettoweb.dao;

import it.unical.progettoweb.model.Review;

import java.util.List;

public interface ReviewDao extends Dao<Review, Integer> {
    List<Review> findByRealEstateId(int realEstateId);
    List<Review> findByUserId(int userId);
    Double getAverageRatingForRealEstate(int realEstateId);
}